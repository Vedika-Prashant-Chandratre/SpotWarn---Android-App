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

public class AddParkingActivity extends AppCompatActivity {

    EditText etSlotName, etLocation;
    Button btnAdd;
    DatabaseReference dbRef;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_parking);

        etSlotName = findViewById(R.id.etSlotName);
        etLocation = findViewById(R.id.etLocation);
        btnAdd = findViewById(R.id.btnAddParking);

        mAuth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference("parking_slots");

        btnAdd.setOnClickListener(v -> addParkingSlot());
    }

    private void addParkingSlot() {
        String name = etSlotName.getText().toString().trim();
        String location = etLocation.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            etSlotName.setError("Enter Slot Name");
            return;
        }
        if (TextUtils.isEmpty(location)) {
            etLocation.setError("Enter Location");
            return;
        }

        String slotId = dbRef.push().getKey();
        ParkingSlot slot = new ParkingSlot(slotId, name, location, "free", mAuth.getCurrentUser().getUid());

        dbRef.child(slotId).setValue(slot)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Parking Slot Added", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show()
                );
    }
}