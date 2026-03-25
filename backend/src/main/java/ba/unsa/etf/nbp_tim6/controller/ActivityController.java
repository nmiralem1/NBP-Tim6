package ba.unsa.etf.nbp_tim6.controller;

import ba.unsa.etf.nbp_tim6.model.Activity;
import ba.unsa.etf.nbp_tim6.service.abstraction.ActivityService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {

    private final ActivityService activityService;

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @PostMapping
    public String createActivity(@RequestBody Activity activity) {
        activityService.createActivity(activity);
        return "Activity created!";
    }

    @GetMapping("/{id}")
    public Activity getActivityById(@PathVariable Integer id) {
        return activityService.getActivityById(id);
    }

    @GetMapping("/trip/{tripId}")
    public List<Activity> getActivitiesByTripId(@PathVariable Integer tripId) {
        return activityService.getActivitiesByTripId(tripId);
    }

    @PutMapping("/{id}")
    public String updateActivity(@PathVariable Integer id, @RequestBody Activity activity) {
        activity.setId(id);
        activityService.updateActivity(activity);
        return "Activity updated!";
    }

    @DeleteMapping("/{id}")
    public String deleteActivity(@PathVariable Integer id) {
        activityService.deleteActivity(id);
        return "Activity deleted!";
    }
}
