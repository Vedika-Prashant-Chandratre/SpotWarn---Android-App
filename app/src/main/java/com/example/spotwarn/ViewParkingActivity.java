package com.example.spotwarn;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.*;
import java.util.ArrayList;

public class ViewParkingActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ParkingAdapter adapter;
    ArrayList<ParkingSlot> slotList;
    DatabaseReference dbRef;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_parking);

        recyclerView = findViewById(R.id.recyclerParking);
        progressBar = findViewById(R.id.progressBar);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        slotList = new ArrayList<>();
        adapter = new ParkingAdapter(slotList, this);
        recyclerView.setAdapter(adapter);

        dbRef = FirebaseDatabase.getInstance().getReference("parking_slots");
        fetchParkingSlots();
    }

    private void fetchParkingSlots() {
        progressBar.setVisibility(View.VISIBLE);

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                slotList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ParkingSlot slot = ds.getValue(ParkingSlot.class);
                    if (slot != null) slotList.add(slot);
                }
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ViewParkingActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}