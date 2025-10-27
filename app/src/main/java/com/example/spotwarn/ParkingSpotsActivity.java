package com.example.spotwarn;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.*;
import java.util.ArrayList;

public class ParkingSpotsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ParkingAdapter adapter;
    ArrayList<ParkingSlot> slotList;
    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_spots);

        recyclerView = findViewById(R.id.recyclerParking);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        slotList = new ArrayList<>();
        adapter = new ParkingAdapter(slotList, this); // Admin can also book/free
        recyclerView.setAdapter(adapter);

        dbRef = FirebaseDatabase.getInstance().getReference("parking_slots");
        loadParkingSlots();
    }

    private void loadParkingSlots() {
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                slotList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ParkingSlot slot = ds.getValue(ParkingSlot.class);
                    if (slot != null) {
                        slotList.add(slot);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(ParkingSpotsActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}