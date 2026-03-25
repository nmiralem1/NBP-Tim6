package ba.unsa.etf.nbp_tim6.service.abstraction;

import ba.unsa.etf.nbp_tim6.model.Transport;
import java.util.List;

public interface TransportService {
    List<Transport> getAll();

    Transport getById(Integer id);

    List<Transport> getByTripId(Integer tripId);

    void create(Transport transport);

    void update(Transport transport);

    void delete(Integer id);
}
