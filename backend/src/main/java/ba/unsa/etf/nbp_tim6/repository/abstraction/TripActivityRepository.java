package ba.unsa.etf.nbp_tim6.repository.abstraction;

import ba.unsa.etf.nbp_tim6.model.TripActivity;
import java.util.List;

public interface TripActivityRepository {
    int save(TripActivity tripActivity);
    List<TripActivity> findByTripId(Integer tripId);
    int delete(Integer id);
}
