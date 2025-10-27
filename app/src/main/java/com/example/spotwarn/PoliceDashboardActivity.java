package com.example.spotwarn;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class PoliceDashboardActivity extends AppCompatActivity {

    CardView cardSafeSpots, cardPoliceInfo, cardHelplines, cardComplaints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_police_dashboard);


        cardSafeSpots = findViewById(R.id.cardSafeSpots);
        cardPoliceInfo = findViewById(R.id.cardPoliceInfo);
        cardHelplines = findViewById(R.id.cardHelplines);
        cardComplaints = findViewById(R.id.cardComplaints);


        cardSafeSpots.setOnClickListener(v -> {
            Intent intent = new Intent(PoliceDashboardActivity.this, SafeSpotsActivity.class);
            startActivity(intent);
        });


        cardPoliceInfo.setOnClickListener(v -> {
            Intent intent = new Intent(PoliceDashboardActivity.this, PoliceInfoActivity.class);
            startActivity(intent);
        });


        cardHelplines.setOnClickListener(v -> {
            Intent intent = new Intent(PoliceDashboardActivity.this, HelplineActivity.class);
            startActivity(intent);
        });


        cardComplaints.setOnClickListener(v -> {
            Intent intent = new Intent(PoliceDashboardActivity.this, ComplaintsActivity.class);
            startActivity(intent);
        });
    }
}
