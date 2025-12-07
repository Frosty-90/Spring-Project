package com.mshreesha.rideshare.controller;

import jakarta.validation.Valid;
import com.mshreesha.rideshare.dto.CreateRideRequest;
import com.mshreesha.rideshare.dto.RideResponse;
import com.mshreesha.rideshare.service.RideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class RideController {

    private final RideService rideService;

    @Autowired
    public RideController(RideService rideService) {
        this.rideService = rideService;
    }

    // USER: Create Ride
    @PostMapping("/rides")
    public RideResponse createRide(@Valid @RequestBody CreateRideRequest request) {
        return rideService.requestRide(request);
    }

    // USER: View own rides
    @GetMapping("/user/rides")
    public List<RideResponse> getMyRides() {
        return rideService.getUserRides();
    }

    // USER/DRIVER: Complete ride
    @PostMapping("/rides/{rideId}/complete")
    public RideResponse completeRide(@PathVariable String rideId) {
        return rideService.completeRide(rideId);
    }
}
