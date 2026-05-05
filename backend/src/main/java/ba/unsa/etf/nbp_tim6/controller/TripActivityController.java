package ba.unsa.etf.nbp_tim6.controller;

import ba.unsa.etf.nbp_tim6.model.TripActivity;
import ba.unsa.etf.nbp_tim6.repository.abstraction.TripActivityRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(
            summary = "Get activities by trip ID",
            description = "Returns all activities associated with a specific trip"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trip activities retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Trip not found")
    })
    @GetMapping("/trip/{tripId}")
    public List<TripActivity> getByTrip(
            @Parameter(description = "ID of the trip", example = "5")
            @PathVariable Integer tripId) {
        return tripActivityRepository.findByTripId(tripId);
    }

    @Operation(
            summary = "Add activity to trip",
            description = "Creates a new trip-activity relation and adds an activity to a trip"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Activity added to trip successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid trip-activity data")
    })
    @PostMapping
    public ResponseEntity<String> add(@RequestBody TripActivity tripActivity) {
        tripActivityRepository.save(tripActivity);
        return ResponseEntity.ok("Activity added to trip");
    }

    @Operation(
            summary = "Remove activity from trip",
            description = "Deletes a trip-activity relation and removes an activity from a trip"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Activity removed from trip successfully"),
            @ApiResponse(responseCode = "404", description = "Trip-activity relation not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> remove(
            @Parameter(description = "ID of the trip-activity relation", example = "1")
            @PathVariable Integer id) {
        tripActivityRepository.delete(id);
        return ResponseEntity.ok("Activity removed from trip");
    }
}
