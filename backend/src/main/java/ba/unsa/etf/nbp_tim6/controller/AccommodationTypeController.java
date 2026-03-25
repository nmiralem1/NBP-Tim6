package ba.unsa.etf.nbp_tim6.controller;

import ba.unsa.etf.nbp_tim6.model.AccommodationType;
import ba.unsa.etf.nbp_tim6.service.abstraction.AccommodationTypeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accommodation-types")
public class AccommodationTypeController {

    private final AccommodationTypeService service;

    public AccommodationTypeController(AccommodationTypeService service) {
        this.service = service;
    }

    @GetMapping
    public List<AccommodationType> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public AccommodationType getById(@PathVariable Integer id) {
        return service.getById(id);
    }

    @PostMapping
    public String create(@RequestBody AccommodationType type) {
        service.create(type);
        return "Accommodation type created!";
    }

    @PutMapping("/{id}")
    public String update(@PathVariable Integer id, @RequestBody AccommodationType type) {
        type.setId(id);
        service.update(type);
        return "Accommodation type updated!";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        service.delete(id);
        return "Accommodation type deleted!";
    }
}
