package ba.unsa.etf.nbp_tim6.controller;

import ba.unsa.etf.nbp_tim6.model.TripActivity;
import ba.unsa.etf.nbp_tim6.repository.abstraction.TripActivityRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trip-activities")
@Tag(name = "TripActivity", description = "Link activities to personal trips")
public class TripActivityController {

    private final TripActivityRepository tripActivityRepository;

    public TripActivityController(TripActivityRepository tripActivityRepository) {
        this.tripActivityRepository = tripActivityRepository;
    }

    @GetMapping("/trip/{tripId}")
    public List<TripActivity> getByTrip(@PathVariable Integer tripId) {
        return tripActivityRepository.findByTripId(tripId);
    }

    @PostMapping
    public ResponseEntity<String> add(@RequestBody TripActivity tripActivity) {
        tripActivityRepository.save(tripActivity);
        return ResponseEntity.ok("Activity added to trip");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> remove(@PathVariable Integer id) {
        tripActivityRepository.delete(id);
        return ResponseEntity.ok("Activity removed from trip");
    }
}
