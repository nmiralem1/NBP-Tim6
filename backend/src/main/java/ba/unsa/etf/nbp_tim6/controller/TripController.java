package ba.unsa.etf.nbp_tim6.controller;

import ba.unsa.etf.nbp_tim6.model.Trip;
import ba.unsa.etf.nbp_tim6.service.abstraction.TripService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trips")
public class TripController {

    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    // ➕ CREATE
    @PostMapping
    public String createTrip(@RequestBody Trip trip) {
        tripService.createTrip(trip);
        return "Trip kreiran!";
    }

    // 📋 GET
    @GetMapping("/user/{userId}")
    public List<Trip> getTrips(@PathVariable Integer userId) {
        return tripService.getTripsByUser(userId);
    }
}