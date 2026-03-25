package ba.unsa.etf.nbp_tim6.controller;

import ba.unsa.etf.nbp_tim6.model.Transport;
import ba.unsa.etf.nbp_tim6.service.abstraction.TransportService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transport")
public class TransportController {

    private final TransportService service;

    public TransportController(TransportService service) {
        this.service = service;
    }

    @GetMapping
    public List<Transport> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Transport getById(@PathVariable Integer id) {
        return service.getById(id);
    }

    @GetMapping("/trip/{tripId}")
    public List<Transport> getByTripId(@PathVariable Integer tripId) {
        return service.getByTripId(tripId);
    }

    @PostMapping
    public String create(@RequestBody Transport transport) {
        service.create(transport);
        return "Transport successfully created!";
    }

    @PutMapping("/{id}")
    public String update(@PathVariable Integer id, @RequestBody Transport transport) {
        transport.setId(id);
        service.update(transport);
        return "Transport updated!";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        service.delete(id);
        return "Transport deleted!";
    }
}
