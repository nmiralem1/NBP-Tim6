package ba.unsa.etf.nbp_tim6.repository.abstraction;

import ba.unsa.etf.nbp_tim6.model.Address;
import java.util.List;

public interface AddressRepository {
    List<Address> findAll();

    Address findById(Integer id);

    int save(Address address);

    int update(Address address);

    int delete(Integer id);
}
