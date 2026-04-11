package ba.unsa.etf.nbp_tim6.controller;

import ba.unsa.etf.nbp_tim6.model.City;
import ba.unsa.etf.nbp_tim6.service.abstraction.CityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cities")
@Tag(
        name = "City",
        description = "Endpoints for retrieving and managing cities within countries"
)
public class CityController {

    private final CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @Operation(
            summary = "Get all cities",
            description = "Returns a list of all cities"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cities retrieved successfully")
    })
    @GetMapping
    public List<City> getAllCities() {
        return cityService.getAllCities();
    }

    @Operation(
            summary = "Get city by ID",
            description = "Returns a city based on its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "City retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "City not found")
    })
    @GetMapping("/{id}")
    public City getCityById(
            @Parameter(description = "ID of the city", example = "1")
            @PathVariable Integer id) {
        return cityService.getCityById(id);
    }

    @Operation(
            summary = "Get cities by country ID",
            description = "Returns all cities belonging to a specific country"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cities retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Country not found")
    })
    @GetMapping("/country/{countryId}")
    public List<City> getCitiesByCountry(
            @Parameter(description = "ID of the country", example = "2")
            @PathVariable Integer countryId) {
        return cityService.getCitiesByCountry(countryId);
    }

    @Operation(
            summary = "Create city",
            description = "Creates a new city"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "City created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid city data")
    })
    @PostMapping
    public String createCity(@Valid @RequestBody City city) {
        cityService.createCity(city);
        return "City created!";
    }

    @Operation(
            summary = "Update city",
            description = "Updates an existing city by its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "City updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid city data"),
            @ApiResponse(responseCode = "404", description = "City not found")
    })
    @PutMapping("/{id}")
    public String updateCity(
            @Parameter(description = "ID of the city", example = "1")
            @PathVariable Integer id,
            @Valid @RequestBody City city) {
        city.setId(id);
        cityService.updateCity(city);
        return "City updated!";
    }

    @Operation(
            summary = "Delete city",
            description = "Deletes a city by its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "City deleted successfully"),
            @ApiResponse(responseCode = "404", description = "City not found")
    })
    @DeleteMapping("/{id}")
    public String deleteCity(
            @Parameter(description = "ID of the city", example = "1")
            @PathVariable Integer id) {
        cityService.deleteCity(id);
        return "City deleted!";
    }
}