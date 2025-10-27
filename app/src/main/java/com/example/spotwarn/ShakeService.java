package com.example.spotwarn;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShakeService extends Service implements SensorEventListener {

    private static final String CHANNEL_ID = "ShakeServiceChannel";
    private static final float SHAKE_THRESHOLD = 12.0f;
    private static final int SHAKE_COUNT_RESET_TIME_MS = 3000;

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private long lastShakeTime = 0;
    private List<String> phoneNumbers = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        startForegroundNotification();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }

        phoneNumbers = loadNumbersFromLocalStorage();
        fetchPhoneNumbersFromFirebase();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && "STOP".equals(intent.getAction())) stopSelf();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (sensorManager != null) sensorManager.unregisterListener(this);
        Toast.makeText(this, "Shake detection stopped.", Toast.LENGTH_SHORT).show();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) { return null; }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER) return;

        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        double acceleration = Math.sqrt(x*x + y*y + z*z) - SensorManager.GRAVITY_EARTH;

        if (acceleration > SHAKE_THRESHOLD) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastShakeTime > SHAKE_COUNT_RESET_TIME_MS) {
                lastShakeTime = currentTime;
                sendHelpMessage();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    private void sendHelpMessage() {
        if (phoneNumbers.isEmpty()) return;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) return;

        String message = "🚨 Emergency! I need help! - SpotWarn";
        for (String num : phoneNumbers) {
            try { SmsManager.getDefault().sendTextMessage(num, null, message, null, null); }
            catch (Exception e) { e.printStackTrace(); }
        }
        Toast.makeText(this, "Help message sent!", Toast.LENGTH_SHORT).show();
    }

    private void fetchPhoneNumbersFromFirebase() {
        String uid = FirebaseAuth.getInstance().getCurrentUser() != null
                ? FirebaseAuth.getInstance().getCurrentUser().getUid() : null;
        if (uid == null) return;

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Info").child(uid);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    phoneNumbers.clear();
                    for (int i = 1; i <= 5; i++) {
                        String num = snapshot.child("contact" + i).getValue(String.class);
                        if (num != null && !num.isEmpty()) phoneNumbers.add(num);
                    }
                    saveNumbersLocally(phoneNumbers);
                }
            }
            @Override public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void saveNumbersLocally(List<String> numbers) {
        SharedPreferences.Editor editor = getSharedPreferences("SpotWarnInfo", MODE_PRIVATE).edit();
        editor.putString("phoneNumbers", String.join(",", numbers));
        editor.apply();
    }

    private List<String> loadNumbersFromLocalStorage() {
        String saved = getSharedPreferences("SpotWarnInfo", MODE_PRIVATE)
                .getString("phoneNumbers", "");
        if (saved.isEmpty()) return new ArrayList<>();
        return new ArrayList<>(Arrays.asList(saved.split(",")));
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Shake Detection Service",
                    NotificationManager.IMPORTANCE_HIGH
            );
            getSystemService(NotificationManager.class).createNotificationChannel(channel);
        }
    }

    private void startForegroundNotification() {
        Intent stopIntent = new Intent(this, ShakeService.class);
        stopIntent.setAction("STOP");
        PendingIntent stopPending = PendingIntent.getService(
                this, 0, stopIntent,
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ?
                        PendingIntent.FLAG_IMMUTABLE : 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("SpotWarn Active")
                .setContentText("Shake detection is running for your safety")
                .setSmallIcon(R.drawable.logo) // make sure logo exists
                .addAction(R.drawable.logo, "Stop", stopPending)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();

        startForeground(1, notification);
    }
}
