package ba.unsa.etf.nbp_tim6.controller;

import ba.unsa.etf.nbp_tim6.model.Address;
import ba.unsa.etf.nbp_tim6.service.abstraction.AddressService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping
    public List<Address> getAllAddresses() {
        return addressService.getAllAddresses();
    }

    @GetMapping("/{id}")
    public Address getAddressById(@PathVariable Integer id) {
        return addressService.getAddressById(id);
    }

    @PostMapping
    public String createAddress(@RequestBody Address address) {
        addressService.createAddress(address);
        return "Address created!";
    }

    @PutMapping("/{id}")
    public String updateAddress(@PathVariable Integer id, @RequestBody Address address) {
        address.setId(id);
        addressService.updateAddress(address);
        return "Address updated!";
    }

    @DeleteMapping("/{id}")
    public String deleteAddress(@PathVariable Integer id) {
        addressService.deleteAddress(id);
        return "Address deleted!";
    }
}
