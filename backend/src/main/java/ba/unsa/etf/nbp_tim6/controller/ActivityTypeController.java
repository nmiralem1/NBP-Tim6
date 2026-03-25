package ba.unsa.etf.nbp_tim6.controller;

import ba.unsa.etf.nbp_tim6.model.ActivityType;
import ba.unsa.etf.nbp_tim6.service.abstraction.ActivityTypeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activity-types")
public class ActivityTypeController {

    private final ActivityTypeService service;

    public ActivityTypeController(ActivityTypeService service) {
        this.service = service;
    }

    @GetMapping
    public List<ActivityType> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ActivityType getById(@PathVariable Integer id) {
        return service.getById(id);
    }

    @PostMapping
    public String create(@RequestBody ActivityType type) {
        service.create(type);
        return "Activity type created!";
    }

    @PutMapping("/{id}")
    public String update(@PathVariable Integer id, @RequestBody ActivityType type) {
        type.setId(id);
        service.update(type);
        return "Activity type updated!";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        service.delete(id);
        return "Activity type deleted!";
    }
}
