package ba.unsa.etf.nbp_tim6.repository.abstraction;

import ba.unsa.etf.nbp_tim6.model.Activity;
import java.util.List;

public interface ActivityRepository {
    int save(Activity activity);

    Activity findById(Integer id);

    List<Activity> findByTripId(Integer tripId);

    int update(Activity activity);

    int delete(Integer id);
}
