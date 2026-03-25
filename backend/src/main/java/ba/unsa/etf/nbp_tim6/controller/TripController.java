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

    // CREATE
    @PostMapping
    public String createTrip(@RequestBody Trip trip) {
        tripService.createTrip(trip);
        return "Trip kreiran!";
    }

    // GET by user
    @GetMapping("/user/{userId}")
    public List<Trip> getTrips(@PathVariable Integer userId) {
        return tripService.getTripsByUser(userId);
    }

    // GET by ID
    @GetMapping("/{id}")
    public Trip getTripById(@PathVariable Integer id) {
        return tripService.getTripById(id);
    }

    // UPDATE
    @PutMapping("/{id}")
    public String updateTrip(@PathVariable Integer id, @RequestBody Trip trip) {
        trip.setId(id);
        tripService.updateTrip(trip);
        return "Trip ažuriran!";
    }

    // DELETE
    @DeleteMapping("/{id}")
    public String deleteTrip(@PathVariable Integer id) {
        tripService.deleteTrip(id);
        return "Trip obrisan!";
    }
}