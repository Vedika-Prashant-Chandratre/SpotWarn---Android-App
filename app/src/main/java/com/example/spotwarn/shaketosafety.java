package com.example.spotwarn;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class shaketosafety extends AppCompatActivity {

    private ActivityResultLauncher<String> requestSmsPermissionLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Redirect to FillInfo if no contacts saved
        SharedPreferences preferences = getSharedPreferences("SpotWarnInfo", MODE_PRIVATE);
        if (preferences.getString("phoneNumbers", "").isEmpty()) {
            startActivity(new Intent(this, FillInfo.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_shaketosafety);

        // Register SMS permission launcher
        requestSmsPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) startShakeService();
                    else Toast.makeText(this, "SMS permission required!", Toast.LENGTH_LONG).show();
                });

        // Request SMS permission if not granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            requestSmsPermissionLauncher.launch(Manifest.permission.SEND_SMS);
        } else {
            startShakeService();
        }
    }

    private void startShakeService() {
        Intent serviceIntent = new Intent(this, ShakeService.class);
        ContextCompat.startForegroundService(this, serviceIntent);
        Toast.makeText(this, "Shake detection service started.", Toast.LENGTH_SHORT).show();
    }
}

