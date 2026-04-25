package ba.unsa.etf.nbp_tim6.controller;

import ba.unsa.etf.nbp_tim6.model.Activity;
import ba.unsa.etf.nbp_tim6.service.abstraction.ActivityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
@Tag(
        name = "Activity",
        description = "Endpoints for managing activities within trips such as tours, events, and experiences"
)
public class ActivityController {

    private final ActivityService activityService;

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @Operation(
            summary = "Create activity",
            description = "Creates a new activity for a trip"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Activity created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid activity data")
    })
    @GetMapping
    public List<Activity> getAllActivities() {
        return activityService.getAllActivities();
    }

    @PostMapping
    public String createActivity(@Valid @RequestBody Activity activity) {
        activityService.createActivity(activity);
        return "Activity created!";
    }

    @Operation(
            summary = "Get activity by ID",
            description = "Returns an activity based on its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Activity retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Activity not found")
    })
    @GetMapping("/{id}")
    public Activity getActivityById(
            @Parameter(description = "ID of the activity", example = "1")
            @PathVariable Integer id) {
        return activityService.getActivityById(id);
    }

    @Operation(
            summary = "Get activities by trip ID",
            description = "Returns all activities associated with a specific trip"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Activities retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Trip not found")
    })
    @GetMapping("/trip/{tripId}")
    public List<Activity> getActivitiesByTripId(
            @Parameter(description = "ID of the trip", example = "5")
            @PathVariable Integer tripId) {
        return activityService.getActivitiesByTripId(tripId);
    }

    @Operation(
            summary = "Update activity",
            description = "Updates an existing activity by its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Activity updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid activity data"),
            @ApiResponse(responseCode = "404", description = "Activity not found")
    })
    @PutMapping("/{id}")
    public String updateActivity(
            @Parameter(description = "ID of the activity", example = "1")
            @PathVariable Integer id,
            @Valid @RequestBody Activity activity) {
        activity.setId(id);
        activityService.updateActivity(activity);
        return "Activity updated!";
    }

    @Operation(
            summary = "Delete activity",
            description = "Deletes an activity by its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Activity deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Activity not found")
    })
    @DeleteMapping("/{id}")
    public String deleteActivity(
            @Parameter(description = "ID of the activity", example = "1")
            @PathVariable Integer id) {
        activityService.deleteActivity(id);
        return "Activity deleted!";
    }
}