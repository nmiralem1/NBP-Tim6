package ba.unsa.etf.nbp_tim6.controller;

import ba.unsa.etf.nbp_tim6.model.ActivityType;
import ba.unsa.etf.nbp_tim6.service.abstraction.ActivityTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activity-types")
@Tag(
        name = "Activity Type",
        description = "Endpoints for managing activity types such as sightseeing, adventure, or cultural activities"
)
public class ActivityTypeController {

    private final ActivityTypeService service;

    public ActivityTypeController(ActivityTypeService service) {
        this.service = service;
    }

    @Operation(
            summary = "Get all activity types",
            description = "Returns a list of all activity types"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Activity types retrieved successfully")
    })
    @GetMapping
    public List<ActivityType> getAll() {
        return service.getAll();
    }

    @Operation(
            summary = "Get activity type by ID",
            description = "Returns one activity type based on its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Activity type retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Activity type not found")
    })
    @GetMapping("/{id}")
    public ActivityType getById(
            @Parameter(description = "ID of the activity type", example = "1")
            @PathVariable Integer id) {
        return service.getById(id);
    }

    @Operation(
            summary = "Create activity type",
            description = "Creates a new activity type"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Activity type created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid activity type data")
    })
    @PostMapping
    public String create(@Valid @RequestBody ActivityType type) {
        service.create(type);
        return "Activity type created!";
    }

    @Operation(
            summary = "Update activity type",
            description = "Updates an existing activity type by its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Activity type updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid activity type data"),
            @ApiResponse(responseCode = "404", description = "Activity type not found")
    })
    @PutMapping("/{id}")
    public String update(
            @Parameter(description = "ID of the activity type", example = "1")
            @PathVariable Integer id,
            @Valid @RequestBody ActivityType type) {
        type.setId(id);
        service.update(type);
        return "Activity type updated!";
    }

    @Operation(
            summary = "Delete activity type",
            description = "Deletes an activity type by its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Activity type deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Activity type not found")
    })
    @DeleteMapping("/{id}")
    public String delete(
            @Parameter(description = "ID of the activity type", example = "1")
            @PathVariable Integer id) {
        service.delete(id);
        return "Activity type deleted!";
    }
}