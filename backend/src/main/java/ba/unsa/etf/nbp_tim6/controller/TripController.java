package ba.unsa.etf.nbp_tim6.controller;

import ba.unsa.etf.nbp_tim6.model.Trip;
import ba.unsa.etf.nbp_tim6.service.abstraction.TripService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trips")
@Tag(
        name = "Trip",
        description = "Endpoints for creating and managing travel plans and itineraries"
)
public class TripController {

    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @Operation(
            summary = "Get all trips",
            description = "Returns a list of all trips"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trips retrieved successfully")
    })
    @GetMapping
    public List<Trip> getAllTrips() {
        return tripService.getAllTrips();
    }

    @Operation(
            summary = "Create trip",
            description = "Creates a new trip"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trip created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid trip data")
    })
    @PostMapping
    public String createTrip(@Valid @RequestBody Trip trip) {
        tripService.createTrip(trip);
        return "Trip created!";
    }

    @Operation(
            summary = "Get trips by user ID",
            description = "Returns all trips associated with a specific user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trips retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/user/{userId}")
    public List<Trip> getTrips(
            @Parameter(description = "ID of the user", example = "7")
            @PathVariable Integer userId) {
        return tripService.getTripsByUser(userId);
    }

    @Operation(
            summary = "Get trip by ID",
            description = "Returns a trip based on its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trip retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Trip not found")
    })
    @GetMapping("/{id}")
    public Trip getTripById(
            @Parameter(description = "ID of the trip", example = "1")
            @PathVariable Integer id) {
        return tripService.getTripById(id);
    }

    @Operation(
            summary = "Update trip",
            description = "Updates an existing trip by its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trip updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid trip data"),
            @ApiResponse(responseCode = "404", description = "Trip not found")
    })
    @PutMapping("/{id}")
    public String updateTrip(
            @Parameter(description = "ID of the trip", example = "1")
            @PathVariable Integer id,
            @Valid @RequestBody Trip trip) {
        trip.setId(id);
        tripService.updateTrip(trip);
        return "Trip updated!";
    }

    @Operation(
            summary = "Delete trip",
            description = "Deletes a trip by its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trip deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Trip not found")
    })
    @DeleteMapping("/{id}")
    public String deleteTrip(
            @Parameter(description = "ID of the trip", example = "1")
            @PathVariable Integer id) {
        tripService.deleteTrip(id);
        return "Trip deleted!";
    }
}