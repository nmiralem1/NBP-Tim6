package ba.unsa.etf.nbp_tim6.repository.abstraction;

import ba.unsa.etf.nbp_tim6.model.TransportType;
import java.util.List;

public interface TransportTypeRepository {
    List<TransportType> findAll();

    TransportType findById(Integer id);

    int save(TransportType transportType);

    int update(TransportType transportType);

    int delete(Integer id);
}
