package com.example.spotwarn;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class UserDashboard extends AppCompatActivity {

    private CardView cardUnsafeZones, cardTraffic, cardParking, cardTimeline, cardEmergency, cardComplaints;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_dashboard);

        // Initialize cards
        cardUnsafeZones = findViewById(R.id.cardUnsafeZones);
        cardTraffic = findViewById(R.id.cardTraffic);
        cardParking = findViewById(R.id.cardParking);
        cardTimeline = findViewById(R.id.cardTimeline);
        cardEmergency = findViewById(R.id.cardEmergency);
        cardComplaints = findViewById(R.id.cardComplaint);

        // Click listeners
        cardUnsafeZones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserDashboard.this, SafeSpotsActivity.class));
            }
        });

        cardTraffic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserDashboard.this, TrafficInfoActivity.class));
            }
        });

        cardParking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserDashboard.this, ParkingSpotsActivity.class));
            }
        });

        cardTimeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserDashboard.this, EventTimelineActivity.class));
            }
        });

        cardEmergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserDashboard.this, shaketosafety.class));
            }
        });

        cardComplaints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserDashboard.this, ComplaintsActivity.class));
            }
        });
    }
}


//package com.example.spotwarn;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.cardview.widget.CardView;
//
//public class UserDashboard extends AppCompatActivity {
//
//    private CardView cardUnsafeZones, cardTraffic, cardParking, cardTimeline, cardEmergency;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_user_dashboard);
//
//        // Initialize cards
//        cardUnsafeZones = findViewById(R.id.cardUnsafeZones);
//        cardTraffic = findViewById(R.id.cardTraffic);
//        cardParking = findViewById(R.id.cardParking);
//        cardTimeline = findViewById(R.id.cardTimeline);
//        cardEmergency = findViewById(R.id.cardEmergency);
//
//        // Click listeners
//        cardUnsafeZones.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(UserDashboard.this, SafeSpotsActivity.class));
//            }
//        });
//
//        cardTraffic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(UserDashboard.this, TrafficInfoActivity.class));
//            }
//        });
//
//        cardParking.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(UserDashboard.this, ParkingSpotsActivity.class));
//            }
//        });
//
//        cardTimeline.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(UserDashboard.this, EventTimelineActivity.class));
//            }
//        });
//
//        cardEmergency.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(UserDashboard.this, shaketosafety.class));
//            }
//        });
//    }
//}