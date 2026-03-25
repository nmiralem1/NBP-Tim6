package ba.unsa.etf.nbp_tim6.controller;

import ba.unsa.etf.nbp_tim6.model.Accommodation;
import ba.unsa.etf.nbp_tim6.service.abstraction.AccommodationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accommodations")
public class AccommodationController {

    private final AccommodationService accommodationService;

    public AccommodationController(AccommodationService accommodationService) {
        this.accommodationService = accommodationService;
    }

    @GetMapping
    public List<Accommodation> getAllAccommodations() {
        return accommodationService.getAllAccommodations();
    }

    @GetMapping("/city/{cityId}")
    public List<Accommodation> getAccommodationsByCityId(@PathVariable Integer cityId) {
        return accommodationService.getAccommodationsByCityId(cityId);
    }

    @GetMapping("/{id}")
    public Accommodation getAccommodationById(@PathVariable Integer id) {
        return accommodationService.getAccommodationById(id);
    }

    @PostMapping
    public String createAccommodation(@RequestBody Accommodation accommodation) {
        accommodationService.createAccommodation(accommodation);
        return "Smještaj kreiran!";
    }

    @PutMapping("/{id}")
    public String updateAccommodation(@PathVariable Integer id, @RequestBody Accommodation accommodation) {
        accommodation.setId(id);
        accommodationService.updateAccommodation(accommodation);
        return "Smještaj ažuriran!";
    }

    @DeleteMapping("/{id}")
    public String deleteAccommodation(@PathVariable Integer id) {
        accommodationService.deleteAccommodation(id);
        return "Smještaj obrisan!";
    }
}