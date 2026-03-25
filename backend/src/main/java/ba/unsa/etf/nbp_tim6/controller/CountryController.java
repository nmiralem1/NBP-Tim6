package ba.unsa.etf.nbp_tim6.controller;

import ba.unsa.etf.nbp_tim6.model.Country;
import ba.unsa.etf.nbp_tim6.service.abstraction.CountryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/countries")
public class CountryController {

    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping
    public List<Country> getAllCountries() {
        return countryService.getAllCountries();
    }

    @GetMapping("/{id}")
    public Country getCountryById(@PathVariable Integer id) {
        return countryService.getCountryById(id);
    }

    @PostMapping
    public String createCountry(@RequestBody Country country) {
        countryService.createCountry(country);
        return "Država kreirana!";
    }

    @PutMapping("/{id}")
    public String updateCountry(@PathVariable Integer id, @RequestBody Country country) {
        country.setId(id);
        countryService.updateCountry(country);
        return "Država ažurirana!";
    }

    @DeleteMapping("/{id}")
    public String deleteCountry(@PathVariable Integer id) {
        countryService.deleteCountry(id);
        return "Država obrisana!";
    }
}