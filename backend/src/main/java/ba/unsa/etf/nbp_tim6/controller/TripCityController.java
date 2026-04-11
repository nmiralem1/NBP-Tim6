package ba.unsa.etf.nbp_tim6.controller;

import ba.unsa.etf.nbp_tim6.model.TripCity;
import ba.unsa.etf.nbp_tim6.service.abstraction.TripCityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trip-cities")
@Tag(
        name = "Trip City",
        description = "Endpoints for managing cities included in a trip itinerary"
)
public class TripCityController {

    private final TripCityService service;

    public TripCityController(TripCityService service) {
        this.service = service;
    }

    @Operation(
            summary = "Get all trip-city relations",
            description = "Returns a list of all trip-city relations"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trip-city relations retrieved successfully")
    })
    @GetMapping
    public List<TripCity> getAll() {
        return service.getAll();
    }

    @Operation(
            summary = "Get trip-city relation by ID",
            description = "Returns a trip-city relation based on its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trip-city relation retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Trip-city relation not found")
    })
    @GetMapping("/{id}")
    public TripCity getById(
            @Parameter(description = "ID of the trip-city relation", example = "1")
            @PathVariable Integer id) {
        return service.getById(id);
    }

    @Operation(
            summary = "Get cities by trip ID",
            description = "Returns all cities associated with a specific trip"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trip cities retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Trip not found")
    })
    @GetMapping("/trip/{tripId}")
    public List<TripCity> getByTripId(
            @Parameter(description = "ID of the trip", example = "5")
            @PathVariable Integer tripId) {
        return service.getByTripId(tripId);
    }

    @Operation(
            summary = "Add city to trip",
            description = "Creates a new trip-city relation and adds a city to a trip itinerary"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "City successfully added to trip"),
            @ApiResponse(responseCode = "400", description = "Invalid trip-city data")
    })
    @PostMapping
    public String create(@Valid @RequestBody TripCity tripCity) {
        service.create(tripCity);
        return "City successfully added to trip!";
    }

    @Operation(
            summary = "Update trip-city relation",
            description = "Updates an existing trip-city relation by its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trip-city relation updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid trip-city data"),
            @ApiResponse(responseCode = "404", description = "Trip-city relation not found")
    })
    @PutMapping("/{id}")
    public String update(
            @Parameter(description = "ID of the trip-city relation", example = "1")
            @PathVariable Integer id,
            @Valid @RequestBody TripCity tripCity) {
        tripCity.setId(id);
        service.update(tripCity);
        return "Trip city connection updated!";
    }

    @Operation(
            summary = "Remove city from trip",
            description = "Deletes a trip-city relation and removes a city from a trip"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "City removed from trip successfully"),
            @ApiResponse(responseCode = "404", description = "Trip-city relation not found")
    })
    @DeleteMapping("/{id}")
    public String delete(
            @Parameter(description = "ID of the trip-city relation", example = "1")
            @PathVariable Integer id) {
        service.delete(id);
        return "City removed from trip!";
    }
}