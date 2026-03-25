package ba.unsa.etf.nbp_tim6.service.abstraction;

import ba.unsa.etf.nbp_tim6.model.TransportType;
import java.util.List;

public interface TransportTypeService {
    List<TransportType> getAll();

    TransportType getById(Integer id);

    void create(TransportType type);

    void update(TransportType type);

    void delete(Integer id);
}
