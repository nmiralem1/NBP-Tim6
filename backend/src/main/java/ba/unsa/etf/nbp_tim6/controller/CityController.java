package ba.unsa.etf.nbp_tim6.controller;

import ba.unsa.etf.nbp_tim6.model.City;
import ba.unsa.etf.nbp_tim6.service.abstraction.CityService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cities")
public class CityController {

    private final CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping
    public List<City> getAllCities() {
        return cityService.getAllCities();
    }

    @GetMapping("/{id}")
    public City getCityById(@PathVariable Integer id) {
        return cityService.getCityById(id);
    }

    @GetMapping("/country/{countryId}")
    public List<City> getCitiesByCountry(@PathVariable Integer countryId) {
        return cityService.getCitiesByCountry(countryId);
    }

    @PostMapping
    public String createCity(@RequestBody City city) {
        cityService.createCity(city);
        return "Grad kreiran!";
    }

    @PutMapping("/{id}")
    public String updateCity(@PathVariable Integer id, @RequestBody City city) {
        city.setId(id);
        cityService.updateCity(city);
        return "Grad ažuriran!";
    }

    @DeleteMapping("/{id}")
    public String deleteCity(@PathVariable Integer id) {
        cityService.deleteCity(id);
        return "Grad obrisan!";
    }
}
