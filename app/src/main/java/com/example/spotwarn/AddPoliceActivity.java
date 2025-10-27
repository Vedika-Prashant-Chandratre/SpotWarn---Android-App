package com.example.spotwarn;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.UUID;

public class AddPoliceActivity extends AppCompatActivity {

    private EditText etPoliceName, etPoliceEmail, etPolicePhone;
    private Button btnAddPolice;

    private DatabaseReference policeRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_police);

        etPoliceName = findViewById(R.id.etPoliceName);
        etPoliceEmail = findViewById(R.id.etPoliceEmail);
        etPolicePhone = findViewById(R.id.etPolicePhone);
        btnAddPolice = findViewById(R.id.btnAddPolice);

        // Firebase reference to "Police" table
        policeRef = FirebaseDatabase.getInstance().getReference("Police");

        btnAddPolice.setOnClickListener(v -> addPolice());
    }

    private void addPolice() {
        String name = etPoliceName.getText().toString().trim();
        String email = etPoliceEmail.getText().toString().trim();
        String phone = etPolicePhone.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Generate unique key for police login
        String policeKey = UUID.randomUUID().toString().substring(0, 8);

        // Create police data map
        HashMap<String, Object> policeData = new HashMap<>();
        policeData.put("name", name);
        policeData.put("email", email);
        policeData.put("phone", phone);
        policeData.put("loginKey", policeKey);

        // Push to Firebase
        policeRef.child(phone).setValue(policeData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Police added. Login Key: " + policeKey, Toast.LENGTH_LONG).show();
                    etPoliceName.setText("");
                    etPoliceEmail.setText("");
                    etPolicePhone.setText("");
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to add police: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
