package com.example.spotwarn;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class PoliceComplaintsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ComplaintAdapter adapter;
    private ArrayList<ComplaintModel> complaintList;
    private DatabaseReference complaintsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_police_complaints);

        recyclerView = findViewById(R.id.recyclerComplaints);
        progressBar = findViewById(R.id.progressBar);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2)); // 2-column grid
        complaintList = new ArrayList<>();
        adapter = new ComplaintAdapter(complaintList);
        recyclerView.setAdapter(adapter);

        complaintsRef = FirebaseDatabase.getInstance().getReference("UnsafeSpots");
        loadComplaints();
    }

    private void loadComplaints() {
        progressBar.setVisibility(View.VISIBLE);

        complaintsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                complaintList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    ComplaintModel model = data.getValue(ComplaintModel.class);
                    if (model != null) complaintList.add(model);
                }
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(PoliceComplaintsActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // =============================
    // Complaint Model
    // =============================
    public static class ComplaintModel {
        public String description, location, reason, reporterId;
        public long timestamp;

        public ComplaintModel() {} // Required for Firebase

        public String getFormattedTime() {
            try {
                return new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
                        .format(new Date(timestamp));
            } catch (Exception e) {
                return "";
            }
        }
    }

    // =============================
    // Adapter Class
    // =============================
    public static class ComplaintAdapter extends RecyclerView.Adapter<ComplaintAdapter.ViewHolder> {
        private final ArrayList<ComplaintModel> list;

        public ComplaintAdapter(ArrayList<ComplaintModel> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull android.view.ViewGroup parent, int viewType) {
            View view = android.view.LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_police_complaints_card, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            ComplaintModel item = list.get(position);
            holder.location.setText("📍 Location: " + item.location);
            holder.reason.setText("Reason: " + item.reason);
            holder.desc.setText("Description: " + item.description);
            holder.reporter.setText("Reporter ID: " + item.reporterId);
            holder.time.setText(item.getFormattedTime());
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            android.widget.TextView location, reason, desc, reporter, time;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                location = itemView.findViewById(R.id.textLocation);
                reason = itemView.findViewById(R.id.textReason);
                desc = itemView.findViewById(R.id.textDesc);
                reporter = itemView.findViewById(R.id.textReporter);
                time = itemView.findViewById(R.id.textTime);
            }
        }
    }
}