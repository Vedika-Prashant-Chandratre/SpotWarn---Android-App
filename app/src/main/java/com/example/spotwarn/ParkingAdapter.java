package com.example.spotwarn;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;

public class ParkingAdapter extends RecyclerView.Adapter<ParkingAdapter.ParkingViewHolder> {

    private ArrayList<ParkingSlot> slotList;
    private Context context;
    private DatabaseReference dbRef;
    private FirebaseAuth mAuth;

    public ParkingAdapter(ArrayList<ParkingSlot> slotList, Context context) {
        this.slotList = slotList;
        this.context = context;
        dbRef = FirebaseDatabase.getInstance().getReference("parking_slots");
        mAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public ParkingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.parking_slot_item, parent, false);
        return new ParkingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParkingViewHolder holder, int position) {
        ParkingSlot slot = slotList.get(position);

        holder.tvSlotName.setText("Slot: " + slot.getSlotName());
        holder.tvLocation.setText("Location: " + slot.getLocation());
        holder.tvStatus.setText(slot.getStatus());

        // Button text
        if (slot.getStatus().equals("free")) {
            holder.btnAction.setText("Book");
            holder.tvStatus.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
        } else {
            holder.btnAction.setText("Free");
            holder.tvStatus.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
        }

        holder.btnAction.setOnClickListener(v -> {
            String newStatus;
            String newUserId;

            if (slot.getStatus().equals("free")) {
                newStatus = "booked";
                newUserId = mAuth.getCurrentUser().getUid();
            } else {
                newStatus = "free";
                newUserId = "";
            }

            slot.setStatus(newStatus);
            slot.setUserId(newUserId);

            dbRef.child(slot.getSlotId()).setValue(slot)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(context, "Slot " + newStatus, Toast.LENGTH_SHORT).show();
                        notifyItemChanged(position);
                    })
                    .addOnFailureListener(e -> Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        });
    }

    @Override
    public int getItemCount() {
        return slotList.size();
    }

    public static class ParkingViewHolder extends RecyclerView.ViewHolder {
        TextView tvSlotName, tvLocation, tvStatus;
        Button btnAction;

        public ParkingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSlotName = itemView.findViewById(R.id.tvSlotName);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnAction = itemView.findViewById(R.id.btnAction);
        }
    }
}