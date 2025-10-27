package com.example.spotwarn;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SafeSpotsActivity extends AppCompatActivity {
    private Button btnAddSpot, btnViewSpots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe_spots);

        btnAddSpot = findViewById(R.id.btnAddSpot);
        btnViewSpots = findViewById(R.id.btnViewSpots);

        // Navigate to Add Spot
        btnAddSpot.setOnClickListener(v -> {
            Intent intent = new Intent(SafeSpotsActivity.this, AddUnsafeSpot.class);
            startActivity(intent);
        });

        // we will implement later
        btnViewSpots.setOnClickListener(v -> {
            // TODO: Later integrate Google Maps here
        });
    }
}