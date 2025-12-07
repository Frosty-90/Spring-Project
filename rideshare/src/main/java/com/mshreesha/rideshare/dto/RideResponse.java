package com.mshreesha.rideshare.dto;

public class RideResponse {

    private String id;
    private String userId;
    private String driverId;
    private String pickupLocation;
    private String dropLocation;
    private String status;

    public RideResponse() {
    }

    public RideResponse(String id, String userId, String driverId,
                        String pickupLocation, String dropLocation,
                        String status) {
        this.id = id;
        this.userId = userId;
        this.driverId = driverId;
        this.pickupLocation = pickupLocation;
        this.dropLocation = dropLocation;
        this.status = status;
    }

    // getters & setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public String getDropLocation() {
        return dropLocation;
    }

    public void setDropLocation(String dropLocation) {
        this.dropLocation = dropLocation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
