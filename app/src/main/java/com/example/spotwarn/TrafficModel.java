package com.example.spotwarn;

public class TrafficModel {
    private String areaName;
    private String status;
    private String time;
    private String lat;
    private String lng;

    public TrafficModel() {
        // Required empty constructor for Firebase
    }

    public TrafficModel(String areaName, String status, String time, String lat, String lng) {
        this.areaName = areaName;
        this.status = status;
        this.time = time;
        this.lat = lat;
        this.lng = lng;
    }

    public String getAreaName() {
        return areaName;
    }

    public String getStatus() {
        return status;
    }

    public String getTime() {
        return time;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }
}
