package ba.unsa.etf.nbp_tim6.controller;

import ba.unsa.etf.nbp_tim6.model.Accommodation;
import ba.unsa.etf.nbp_tim6.service.abstraction.AccommodationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accommodations")
@Tag(
        name = "Accommodation",
        description = "Endpoints for managing accommodations such as hotels, apartments, and rooms"
)
public class AccommodationController {

    private final AccommodationService accommodationService;

    public AccommodationController(AccommodationService accommodationService) {
        this.accommodationService = accommodationService;
    }

    @Operation(
            summary = "Get all accommodations",
            description = "Returns a list of all accommodations"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Accommodations retrieved successfully")
    })
    @GetMapping
    public List<Accommodation> getAllAccommodations() {
        return accommodationService.getAllAccommodations();
    }

    @Operation(
            summary = "Get accommodations by city ID",
            description = "Returns all accommodations located in the specified city"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Accommodations retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "City not found")
    })
    @GetMapping("/city/{cityId}")
    public List<Accommodation> getAccommodationsByCityId(
            @Parameter(description = "ID of the city", example = "3")
            @PathVariable Integer cityId) {
        return accommodationService.getAccommodationsByCityId(cityId);
    }

    @Operation(
            summary = "Get accommodation by ID",
            description = "Returns one accommodation based on its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Accommodation retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Accommodation not found")
    })
    @GetMapping("/{id}")
    public Accommodation getAccommodationById(
            @Parameter(description = "ID of the accommodation", example = "1")
            @PathVariable Integer id) {
        return accommodationService.getAccommodationById(id);
    }

    @Operation(
            summary = "Create accommodation",
            description = "Creates a new accommodation"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Accommodation created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid accommodation data",
                    content = @Content(schema = @Schema()))
    })
    @PostMapping
    public String createAccommodation(
            @Valid @org.springframework.web.bind.annotation.RequestBody Accommodation accommodation) {
        accommodationService.createAccommodation(accommodation);
        return "Accommodation created!";
    }

    @Operation(
            summary = "Update accommodation",
            description = "Updates an existing accommodation by its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Accommodation updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid accommodation data",
                    content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "404", description = "Accommodation not found")
    })
    @PutMapping("/{id}")
    public String updateAccommodation(
            @Parameter(description = "ID of the accommodation", example = "1")
            @PathVariable Integer id,
            @Valid @org.springframework.web.bind.annotation.RequestBody Accommodation accommodation) {
        accommodation.setId(id);
        accommodationService.updateAccommodation(accommodation);
        return "Accommodation updated!";
    }

    @Operation(
            summary = "Delete accommodation",
            description = "Deletes an accommodation by its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Accommodation deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Accommodation not found")
    })
    @DeleteMapping("/{id}")
    public String deleteAccommodation(
            @Parameter(description = "ID of the accommodation", example = "1")
            @PathVariable Integer id) {
        accommodationService.deleteAccommodation(id);
        return "Accommodation deleted!";
    }
}