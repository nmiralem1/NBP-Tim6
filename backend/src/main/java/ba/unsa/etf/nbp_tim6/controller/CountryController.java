package ba.unsa.etf.nbp_tim6.controller;

import ba.unsa.etf.nbp_tim6.model.Country;
import ba.unsa.etf.nbp_tim6.service.abstraction.CountryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/countries")
@Tag(
        name = "Country",
        description = "Endpoints for retrieving and managing countries"
)
public class CountryController {

    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @Operation(
            summary = "Get all countries",
            description = "Returns a list of all countries"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Countries retrieved successfully")
    })
    @GetMapping
    public List<Country> getAllCountries() {
        return countryService.getAllCountries();
    }

    @Operation(
            summary = "Get country by ID",
            description = "Returns a country based on its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Country retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Country not found")
    })
    @GetMapping("/{id}")
    public Country getCountryById(
            @Parameter(description = "ID of the country", example = "1")
            @PathVariable Integer id) {
        return countryService.getCountryById(id);
    }

    @Operation(
            summary = "Create country",
            description = "Creates a new country"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Country created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid country data")
    })
    @PostMapping
    public String createCountry(@Valid @RequestBody Country country) {
        countryService.createCountry(country);
        return "Country created!";
    }

    @Operation(
            summary = "Update country",
            description = "Updates an existing country by its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Country updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid country data"),
            @ApiResponse(responseCode = "404", description = "Country not found")
    })
    @PutMapping("/{id}")
    public String updateCountry(
            @Parameter(description = "ID of the country", example = "1")
            @PathVariable Integer id,
            @Valid @RequestBody Country country) {
        country.setId(id);
        countryService.updateCountry(country);
        return "Country updated!";
    }

    @Operation(
            summary = "Delete country",
            description = "Deletes a country by its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Country deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Country not found")
    })
    @DeleteMapping("/{id}")
    public String deleteCountry(
            @Parameter(description = "ID of the country", example = "1")
            @PathVariable Integer id) {
        countryService.deleteCountry(id);
        return "Country deleted!";
    }
}