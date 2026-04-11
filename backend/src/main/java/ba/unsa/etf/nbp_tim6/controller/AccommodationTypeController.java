package ba.unsa.etf.nbp_tim6.controller;

import ba.unsa.etf.nbp_tim6.model.AccommodationType;
import ba.unsa.etf.nbp_tim6.service.abstraction.AccommodationTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accommodation-types")
@Tag(
        name = "Accommodation Type",
        description = "Endpoints for managing accommodation types such as hotel, apartment, hostel, or villa"
)
public class AccommodationTypeController {

    private final AccommodationTypeService service;

    public AccommodationTypeController(AccommodationTypeService service) {
        this.service = service;
    }

    @Operation(
            summary = "Get all accommodation types",
            description = "Returns a list of all accommodation types"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Accommodation types retrieved successfully")
    })
    @GetMapping
    public List<AccommodationType> getAll() {
        return service.getAll();
    }

    @Operation(
            summary = "Get accommodation type by ID",
            description = "Returns one accommodation type based on its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Accommodation type retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Accommodation type not found")
    })
    @GetMapping("/{id}")
    public AccommodationType getById(
            @Parameter(description = "ID of the accommodation type", example = "1")
            @PathVariable Integer id) {
        return service.getById(id);
    }

    @Operation(
            summary = "Create accommodation type",
            description = "Creates a new accommodation type"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Accommodation type created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid accommodation type data")
    })
    @PostMapping
    public String create(@Valid @RequestBody AccommodationType type) {
        service.create(type);
        return "Accommodation type created!";
    }

    @Operation(
            summary = "Update accommodation type",
            description = "Updates an existing accommodation type by its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Accommodation type updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid accommodation type data"),
            @ApiResponse(responseCode = "404", description = "Accommodation type not found")
    })
    @PutMapping("/{id}")
    public String update(
            @Parameter(description = "ID of the accommodation type", example = "1")
            @PathVariable Integer id,
            @Valid @RequestBody AccommodationType type) {
        type.setId(id);
        service.update(type);
        return "Accommodation type updated!";
    }

    @Operation(
            summary = "Delete accommodation type",
            description = "Deletes an accommodation type by its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Accommodation type deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Accommodation type not found")
    })
    @DeleteMapping("/{id}")
    public String delete(
            @Parameter(description = "ID of the accommodation type", example = "1")
            @PathVariable Integer id) {
        service.delete(id);
        return "Accommodation type deleted!";
    }
}