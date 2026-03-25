package ba.unsa.etf.nbp_tim6.repository.abstraction;

import ba.unsa.etf.nbp_tim6.model.TripCity;
import java.util.List;

public interface TripCityRepository {
    List<TripCity> findAll();

    TripCity findById(Integer id);

    List<TripCity> findByTripId(Integer tripId);

    int save(TripCity tripCity);

    int update(TripCity tripCity);

    int delete(Integer id);
}
