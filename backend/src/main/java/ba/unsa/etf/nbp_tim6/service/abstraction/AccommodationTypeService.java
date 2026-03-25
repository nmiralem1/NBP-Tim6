package ba.unsa.etf.nbp_tim6.service.abstraction;

import ba.unsa.etf.nbp_tim6.model.AccommodationType;
import java.util.List;

public interface AccommodationTypeService {
    List<AccommodationType> getAll();

    AccommodationType getById(Integer id);

    void create(AccommodationType type);

    void update(AccommodationType type);

    void delete(Integer id);
}
