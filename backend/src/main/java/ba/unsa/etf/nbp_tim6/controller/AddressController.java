package ba.unsa.etf.nbp_tim6.controller;

import ba.unsa.etf.nbp_tim6.model.Address;
import ba.unsa.etf.nbp_tim6.service.abstraction.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@Tag(
        name = "Address",
        description = "Endpoints for managing addresses"
)
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @Operation(
            summary = "Get all addresses",
            description = "Returns a list of all addresses"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Addresses retrieved successfully")
    })
    @GetMapping
    public List<Address> getAllAddresses() {
        return addressService.getAllAddresses();
    }

    @Operation(
            summary = "Get address by ID",
            description = "Returns one address based on its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Address retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Address not found")
    })
    @GetMapping("/{id}")
    public Address getAddressById(
            @Parameter(description = "ID of the address", example = "1")
            @PathVariable Integer id) {
        return addressService.getAddressById(id);
    }

    @Operation(
            summary = "Create address",
            description = "Creates a new address"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Address created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid address data")
    })
    @PostMapping
    public String createAddress(@Valid @RequestBody Address address) {
        addressService.createAddress(address);
        return "Address created!";
    }

    @Operation(
            summary = "Update address",
            description = "Updates an existing address by its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Address updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid address data"),
            @ApiResponse(responseCode = "404", description = "Address not found")
    })
    @PutMapping("/{id}")
    public String updateAddress(
            @Parameter(description = "ID of the address", example = "1")
            @PathVariable Integer id,
            @Valid @RequestBody Address address) {
        address.setId(id);
        addressService.updateAddress(address);
        return "Address updated!";
    }

    @Operation(
            summary = "Delete address",
            description = "Deletes an address by its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Address deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Address not found")
    })
    @DeleteMapping("/{id}")
    public String deleteAddress(
            @Parameter(description = "ID of the address", example = "1")
            @PathVariable Integer id) {
        addressService.deleteAddress(id);
        return "Address deleted!";
    }
}