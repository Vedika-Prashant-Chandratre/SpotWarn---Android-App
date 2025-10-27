package com.example.spotwarn;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class AdminDashboard extends AppCompatActivity {

    private CardView cardAddPolice, cardViewPolice;
    private CardView cardAddParking, cardViewParking;
    private CardView cardAddTimeline, cardViewTimeline;
    private CardView cardLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard); // your XML file name

        // Initialize all card views
        cardAddPolice = findViewById(R.id.cardAddPolice);
        cardViewPolice = findViewById(R.id.cardViewPolice);
        cardAddParking = findViewById(R.id.cardAddParking);
        cardViewParking = findViewById(R.id.cardViewParking);
        cardAddTimeline = findViewById(R.id.cardAddTimeline);
        cardViewTimeline = findViewById(R.id.cardViewTimeline);
        cardLogout = findViewById(R.id.cardLogout);

        // Set click listeners for each card
        cardAddPolice.setOnClickListener(v -> openAddPolice());
        cardViewPolice.setOnClickListener(v -> openViewPolice());
        cardAddParking.setOnClickListener(v -> openAddParking());
        cardViewParking.setOnClickListener(v -> openViewParking());
        cardAddTimeline.setOnClickListener(v -> openAddTimeline());
        cardViewTimeline.setOnClickListener(v -> openViewTimeline());
        cardLogout.setOnClickListener(v -> logoutAdmin());
    }

    // Methods to open activities (replace with your actual activity classes)
    private void openAddPolice() {
        Intent intent = new Intent(this, AddPoliceActivity.class);
        startActivity(intent);
    }

    private void openViewPolice() {
        Intent intent = new Intent(this, ViewPoliceActivity.class);
        startActivity(intent);
    }

    private void openAddParking() {
        Intent intent = new Intent(this, ParkingSpotsActivity.class);
        startActivity(intent);
    }

    private void openViewParking() {
        Intent intent = new Intent(this, ParkingSpotsActivity.class);
        startActivity(intent);
    }

    private void openAddTimeline() {
        Intent intent = new Intent(this, EventTimelineActivity.class);
        startActivity(intent);
    }

    private void openViewTimeline() {
        Intent intent = new Intent(this, EventTimelineActivity.class);
        startActivity(intent);
    }

    private void logoutAdmin() {
        // You can clear shared preferences or Firebase auth if needed
        Toast.makeText(this, "Logged out successfully!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, AdminLogin.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
