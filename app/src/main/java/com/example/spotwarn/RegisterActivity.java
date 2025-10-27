package com.example.spotwarn;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    EditText etName, etEmail, etDesignation, etPhone, etPassword, etConfirmPassword;
    Button btnRegister;
    TextView tvLogin;

    FirebaseAuth auth;
    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference("Police");

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etDesignation = findViewById(R.id.etDesignation);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);

        btnRegister.setOnClickListener(v -> registerUser());

        tvLogin.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void registerUser() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String designation = etDesignation.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            etName.setError("Enter Full Name");
            return;
        }
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Enter valid Email");
            return;
        }
        if (TextUtils.isEmpty(designation)) {
            etDesignation.setError("Enter Designation");
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            etPhone.setError("Enter Phone Number");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Enter Password");
            return;
        }
        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match");
            return;
        }

        // Create User with FirebaseAuth
        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    String userId = auth.getCurrentUser().getUid();
                    User user = new User(name, email, designation, phone);

                    database.child(userId).setValue(user)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(RegisterActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                finish();
                            })
                            .addOnFailureListener(e ->
                                    Toast.makeText(RegisterActivity.this, "Database Error: " + e.getMessage(), Toast.LENGTH_LONG).show()
                            );

                })
                .addOnFailureListener(e ->
                        Toast.makeText(RegisterActivity.this, "Signup Failed: " + e.getMessage(), Toast.LENGTH_LONG).show()
                );
    }

    // Model class
    public static class User {
        public String name, email, designation, phone;

        public User() {} // Empty constructor required

        public User(String name, String email, String designation, String phone) {
            this.name = name;
            this.email = email;
            this.designation = designation;
            this.phone = phone;
        }
    }
}

