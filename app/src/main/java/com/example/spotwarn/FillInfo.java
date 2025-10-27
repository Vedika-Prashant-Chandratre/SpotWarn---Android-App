package com.example.spotwarn;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class FillInfo extends AppCompatActivity {

    private EditText etUserName, etNum1, etNum2, etNum3, etNum4, etNum5;
    private Button btnSaveInfo;

    private FirebaseAuth auth;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_info);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference("Info");

        etUserName = findViewById(R.id.etUserName);
        etNum1 = findViewById(R.id.etNum1);
        etNum2 = findViewById(R.id.etNum2);
        etNum3 = findViewById(R.id.etNum3);
        etNum4 = findViewById(R.id.etNum4);
        etNum5 = findViewById(R.id.etNum5);
        btnSaveInfo = findViewById(R.id.btnSaveInfo);

        btnSaveInfo.setOnClickListener(v -> saveUserInfo());
    }

    private void saveUserInfo() {
        String name = etUserName.getText().toString().trim();
        String num1 = etNum1.getText().toString().trim();
        String num2 = etNum2.getText().toString().trim();
        String num3 = etNum3.getText().toString().trim();
        String num4 = etNum4.getText().toString().trim();
        String num5 = etNum5.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(num1)) {
            Toast.makeText(this, "Name and at least one contact required", Toast.LENGTH_SHORT).show();
            return;
        }

        List<String> phoneNumbers = new ArrayList<>();
        if (!num1.isEmpty()) phoneNumbers.add(num1);
        if (!num2.isEmpty()) phoneNumbers.add(num2);
        if (!num3.isEmpty()) phoneNumbers.add(num3);
        if (!num4.isEmpty()) phoneNumbers.add(num4);
        if (!num5.isEmpty()) phoneNumbers.add(num5);

        saveToFirebase(name, phoneNumbers);
        saveToLocal(phoneNumbers);

        Toast.makeText(this, "Info saved successfully", Toast.LENGTH_SHORT).show();

        // Redirect to Shake Detection activity
        Intent intent = new Intent(FillInfo.this, shaketosafety.class);
        startActivity(intent);
        finish();
    }

    private void saveToFirebase(String name, List<String> phoneNumbers) {
        String userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;
        if (userId == null) return;

        DatabaseReference infoRef = database.child(userId);
        infoRef.child("name").setValue(name);

        for (int i = 0; i < phoneNumbers.size(); i++) {
            infoRef.child("contact" + (i + 1)).setValue(phoneNumbers.get(i));
        }
    }

    private void saveToLocal(List<String> phoneNumbers) {
        SharedPreferences preferences = getSharedPreferences("SpotWarnInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        StringBuilder numbersString = new StringBuilder();
        for (String number : phoneNumbers) {
            numbersString.append(number).append(",");
        }
        editor.putString("phoneNumbers", numbersString.toString());
        editor.apply();
    }
}


