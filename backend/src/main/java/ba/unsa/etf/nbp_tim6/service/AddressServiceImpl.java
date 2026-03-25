package ba.unsa.etf.nbp_tim6.service;

import ba.unsa.etf.nbp_tim6.model.Address;
import ba.unsa.etf.nbp_tim6.repository.abstraction.AddressRepository;
import ba.unsa.etf.nbp_tim6.service.abstraction.AddressService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    public AddressServiceImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public List<Address> getAllAddresses() {
        return addressRepository.findAll();
    }

    @Override
    public Address getAddressById(Integer id) {
        Address address = addressRepository.findById(id);
        if (address == null) {
            throw new RuntimeException("Address not found!");
        }
        return address;
    }

    @Override
    public void createAddress(Address address) {
        addressRepository.save(address);
    }

    @Override
    public void updateAddress(Address address) {
        addressRepository.update(address);
    }

    @Override
    public void deleteAddress(Integer id) {
        addressRepository.delete(id);
    }
}
