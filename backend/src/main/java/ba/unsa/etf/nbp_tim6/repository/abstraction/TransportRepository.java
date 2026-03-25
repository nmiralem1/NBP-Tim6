package ba.unsa.etf.nbp_tim6.repository.abstraction;

import ba.unsa.etf.nbp_tim6.model.Transport;
import java.util.List;

public interface TransportRepository {
    List<Transport> findAll();

    Transport findById(Integer id);

    List<Transport> findByTripId(Integer tripId);

    int save(Transport transport);

    int update(Transport transport);

    int delete(Integer id);
}
