package com.mshreesha.rideshare.service;

import com.mshreesha.rideshare.dto.CreateRideRequest;
import com.mshreesha.rideshare.dto.RideResponse;
import com.mshreesha.rideshare.exception.BadRequestException;
import com.mshreesha.rideshare.exception.NotFoundException;
import com.mshreesha.rideshare.model.Ride;
import com.mshreesha.rideshare.model.User;
import com.mshreesha.rideshare.repository.RideRepository;
import com.mshreesha.rideshare.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class RideService {

    private final RideRepository rideRepository;
    private final UserRepository userRepository;

    @Autowired
    public RideService(RideRepository rideRepository,
                       UserRepository userRepository) {
        this.rideRepository = rideRepository;
        this.userRepository = userRepository;
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    public RideResponse requestRide(CreateRideRequest request) {
        User current = getCurrentUser();
        if (!"ROLE_USER".equals(current.getRole())) {
            throw new BadRequestException("Only ROLE_USER can request rides");
        }

        Ride ride = new Ride();
        ride.setUserId(current.getId());
        ride.setPickupLocation(request.getPickupLocation());
        ride.setDropLocation(request.getDropLocation());
        ride.setStatus("REQUESTED");
        ride.setCreatedAt(new Date());

        Ride saved = rideRepository.save(ride);
        return toResponse(saved);
    }

    public List<RideResponse> getPendingRidesForDriver() {
        User driver = getCurrentUser();
        if (!"ROLE_DRIVER".equals(driver.getRole())) {
            throw new BadRequestException("Only ROLE_DRIVER can view pending requests");
        }

        List<Ride> rides = rideRepository.findByStatus("REQUESTED");
        List<RideResponse> result = new ArrayList<>();
        for (Ride r : rides) {
            result.add(toResponse(r));
        }
        return result;
    }

    public RideResponse acceptRide(String rideId) {
        User driver = getCurrentUser();
        if (!"ROLE_DRIVER".equals(driver.getRole())) {
            throw new BadRequestException("Only ROLE_DRIVER can accept rides");
        }

        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new NotFoundException("Ride not found"));

        if (!"REQUESTED".equals(ride.getStatus())) {
            throw new BadRequestException("Ride is not in REQUESTED state");
        }

        ride.setDriverId(driver.getId());
        ride.setStatus("ACCEPTED");
        Ride saved = rideRepository.save(ride);

        return toResponse(saved);
    }

    public RideResponse completeRide(String rideId) {
        User current = getCurrentUser();

        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new NotFoundException("Ride not found"));

        if (!"ACCEPTED".equals(ride.getStatus())) {
            throw new BadRequestException("Ride must be ACCEPTED to complete");
        }

        if (!current.getId().equals(ride.getUserId())
                && (ride.getDriverId() == null
                || !current.getId().equals(ride.getDriverId()))) {
            throw new BadRequestException("You are not allowed to complete this ride");
        }

        ride.setStatus("COMPLETED");
        Ride saved = rideRepository.save(ride);
        return toResponse(saved);
    }

    public List<RideResponse> getUserRides() {
        User user = getCurrentUser();
        List<Ride> rides = rideRepository.findByUserId(user.getId());
        List<RideResponse> result = new ArrayList<>();
        for (Ride r : rides) {
            result.add(toResponse(r));
        }
        return result;
    }

    private RideResponse toResponse(Ride ride) {
        return new RideResponse(
                ride.getId(),
                ride.getUserId(),
                ride.getDriverId(),
                ride.getPickupLocation(),
                ride.getDropLocation(),
                ride.getStatus()
        );
    }
}
