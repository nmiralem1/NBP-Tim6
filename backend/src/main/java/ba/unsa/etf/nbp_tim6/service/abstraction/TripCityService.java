package ba.unsa.etf.nbp_tim6.service.abstraction;

import ba.unsa.etf.nbp_tim6.model.TripCity;
import java.util.List;

public interface TripCityService {
    List<TripCity> getAll();

    TripCity getById(Integer id);

    List<TripCity> getByTripId(Integer tripId);

    void create(TripCity tripCity);

    void update(TripCity tripCity);

    void delete(Integer id);
}
