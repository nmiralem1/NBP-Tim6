package ba.unsa.etf.nbp_tim6.repository.abstraction;

import ba.unsa.etf.nbp_tim6.model.AccommodationType;
import java.util.List;

public interface AccommodationTypeRepository {
    List<AccommodationType> findAll();

    AccommodationType findById(Integer id);

    int save(AccommodationType accommodationType);

    int update(AccommodationType accommodationType);

    int delete(Integer id);
}
