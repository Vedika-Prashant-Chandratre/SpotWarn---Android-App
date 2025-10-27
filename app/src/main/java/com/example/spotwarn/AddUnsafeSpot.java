package com.example.spotwarn;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AddUnsafeSpot extends AppCompatActivity {

    private EditText etLocation, etReason, etDescription;
    private Button btnSubmit;

    private DatabaseReference unsafeSpotsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_unsafe_spot);

        etLocation = findViewById(R.id.etLocation);
        etReason = findViewById(R.id.etReason);
        etDescription = findViewById(R.id.etDescription);
        btnSubmit = findViewById(R.id.btnSubmit);

        // Firebase reference to "UnsafeSpots" table
        unsafeSpotsRef = FirebaseDatabase.getInstance().getReference("UnsafeSpots");

        btnSubmit.setOnClickListener(v -> submitSpot());
    }

    private void submitSpot() {
        String location = etLocation.getText().toString().trim();
        String reason = etReason.getText().toString().trim();
        String description = etDescription.getText().toString().trim();

        if (TextUtils.isEmpty(location) || TextUtils.isEmpty(reason)) {
            Toast.makeText(this, "Please enter location and reason", Toast.LENGTH_SHORT).show();
            return;
        }

        // Generate a unique key for each unsafe spot
        String spotId = unsafeSpotsRef.push().getKey();

        if (spotId == null) {
            Toast.makeText(this, "Error generating spot ID", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get reporter ID (police)
        String reporterId = FirebaseAuth.getInstance().getCurrentUser() != null ?
                FirebaseAuth.getInstance().getCurrentUser().getUid() : "unknown";

        // Create data map
        HashMap<String, Object> spotData = new HashMap<>();
        spotData.put("location", location);
        spotData.put("reason", reason);
        spotData.put("description", description);
        spotData.put("reporterId", reporterId);
        spotData.put("timestamp", System.currentTimeMillis());

        // Save to Firebase
        unsafeSpotsRef.child(spotId).setValue(spotData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Unsafe spot reported successfully", Toast.LENGTH_LONG).show();
                    etLocation.setText("");
                    etReason.setText("");
                    etDescription.setText("");
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to report spot: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
