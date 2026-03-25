package ba.unsa.etf.nbp_tim6.controller;

import ba.unsa.etf.nbp_tim6.model.TransportType;
import ba.unsa.etf.nbp_tim6.service.abstraction.TransportTypeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transport-types")
public class TransportTypeController {

    private final TransportTypeService service;

    public TransportTypeController(TransportTypeService service) {
        this.service = service;
    }

    @GetMapping
    public List<TransportType> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public TransportType getById(@PathVariable Integer id) {
        return service.getById(id);
    }

    @PostMapping
    public String create(@RequestBody TransportType type) {
        service.create(type);
        return "Transport type created!";
    }

    @PutMapping("/{id}")
    public String update(@PathVariable Integer id, @RequestBody TransportType type) {
        type.setId(id);
        service.update(type);
        return "Transport type updated!";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        service.delete(id);
        return "Transport type deleted!";
    }
}
