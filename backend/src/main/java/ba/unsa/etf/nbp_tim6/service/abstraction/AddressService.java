package ba.unsa.etf.nbp_tim6.service.abstraction;

import ba.unsa.etf.nbp_tim6.model.Address;
import java.util.List;

public interface AddressService {
    List<Address> getAllAddresses();

    Address getAddressById(Integer id);

    void createAddress(Address address);

    void updateAddress(Address address);

    void deleteAddress(Integer id);
}
