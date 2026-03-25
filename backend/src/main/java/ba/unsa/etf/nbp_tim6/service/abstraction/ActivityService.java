package ba.unsa.etf.nbp_tim6.service.abstraction;

import ba.unsa.etf.nbp_tim6.model.Activity;
import java.util.List;

public interface ActivityService {
    void createActivity(Activity activity);

    Activity getActivityById(Integer id);

    List<Activity> getActivitiesByTripId(Integer tripId);

    void updateActivity(Activity activity);

    void deleteActivity(Integer id);
}
