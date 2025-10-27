package com.example.spotwarn;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class TrafficInfoAdapter extends ArrayAdapter<String> {

    public TrafficInfoAdapter(Context context, ArrayList<String> trafficList) {
        super(context, 0, trafficList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String trafficInfo = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_traffic_info, parent, false);
        }

        TextView textTraffic = convertView.findViewById(R.id.textTraffic);
        textTraffic.setText(trafficInfo);

        return convertView;
    }
}

