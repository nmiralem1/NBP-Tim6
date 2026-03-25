package ba.unsa.etf.nbp_tim6.controller;

import ba.unsa.etf.nbp_tim6.model.TripCity;
import ba.unsa.etf.nbp_tim6.service.abstraction.TripCityService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trip-cities")
public class TripCityController {

    private final TripCityService service;

    public TripCityController(TripCityService service) {
        this.service = service;
    }

    @GetMapping
    public List<TripCity> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public TripCity getById(@PathVariable Integer id) {
        return service.getById(id);
    }

    @GetMapping("/trip/{tripId}")
    public List<TripCity> getByTripId(@PathVariable Integer tripId) {
        return service.getByTripId(tripId);
    }

    @PostMapping
    public String create(@RequestBody TripCity tripCity) {
        service.create(tripCity);
        return "Grad uspješno dodan u putovanje!";
    }

    @PutMapping("/{id}")
    public String update(@PathVariable Integer id, @RequestBody TripCity tripCity) {
        tripCity.setId(id);
        service.update(tripCity);
        return "Veza grada i putovanja ažurirana!";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        service.delete(id);
        return "Grad uklonjen iz putovanja!";
    }
}
