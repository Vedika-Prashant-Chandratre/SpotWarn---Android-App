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

public class ComplaintsActivity extends AppCompatActivity {

    private EditText etTitle, etDescription;
    private Button btnSubmit;

    private DatabaseReference complaintsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaints);

        etTitle = findViewById(R.id.etComplaintTitle);
        etDescription = findViewById(R.id.etComplaintDescription);
        btnSubmit = findViewById(R.id.btnSubmitComplaint);

        // Firebase reference to "Complaints" table
        complaintsRef = FirebaseDatabase.getInstance().getReference("Complaints");

        btnSubmit.setOnClickListener(v -> submitComplaint());
    }

    private void submitComplaint() {
        String title = etTitle.getText().toString().trim();
        String description = etDescription.getText().toString().trim();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description)) {
            Toast.makeText(this, "Please fill both fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Generate unique complaint ID
        String complaintId = complaintsRef.push().getKey();
        if (complaintId == null) {
            Toast.makeText(this, "Error generating complaint ID", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = FirebaseAuth.getInstance().getCurrentUser() != null ?
                FirebaseAuth.getInstance().getCurrentUser().getUid() : "anonymous";

        HashMap<String, Object> complaintData = new HashMap<>();
        complaintData.put("title", title);
        complaintData.put("description", description);
        complaintData.put("userId", userId);
        complaintData.put("timestamp", System.currentTimeMillis());

        complaintsRef.child(complaintId).setValue(complaintData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Complaint filed successfully", Toast.LENGTH_LONG).show();
                    etTitle.setText("");
                    etDescription.setText("");
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to file complaint: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
