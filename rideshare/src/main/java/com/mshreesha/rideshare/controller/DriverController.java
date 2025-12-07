package com.mshreesha.rideshare.controller;

import com.mshreesha.rideshare.dto.RideResponse;
import com.mshreesha.rideshare.service.RideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/driver")
public class DriverController {

    private final RideService rideService;

    @Autowired
    public DriverController(RideService rideService) {
        this.rideService = rideService;
    }

    // DRIVER: View pending ride requests
    @GetMapping("/rides/requests")
    public List<RideResponse> getPendingRides() {
        return rideService.getPendingRidesForDriver();
    }

    // DRIVER: Accept a ride
    @PostMapping("/rides/{rideId}/accept")
    public RideResponse acceptRide(@PathVariable String rideId) {
        return rideService.acceptRide(rideId);
    }
}
