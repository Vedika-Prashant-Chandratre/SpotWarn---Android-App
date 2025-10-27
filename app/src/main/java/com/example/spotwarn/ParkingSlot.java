package com.example.spotwarn;

public class ParkingSlot {
    private String slotId;
    private String slotName;
    private String location;
    private String status; // "free" or "occupied"
    private String addedBy;

    public ParkingSlot() {} // Required for Firebase

    public ParkingSlot(String slotId, String slotName, String location, String status, String addedBy) {
        this.slotId = slotId;
        this.slotName = slotName;
        this.location = location;
        this.status = status;
        this.addedBy = addedBy;
    }

    public String getSlotId() { return slotId; }
    public String getSlotName() { return slotName; }
    public String getLocation() { return location; }
    public String getStatus() { return status; }
    public String getAddedBy() { return addedBy; }

    public void setStatus(String status) { this.status = status; }

    public String getName() {
        return "";
    }

    public void setUserId(String newUserId) {
    }
}