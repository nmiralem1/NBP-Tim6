package ba.unsa.etf.nbp_tim6.repository.abstraction;

import ba.unsa.etf.nbp_tim6.model.ActivityType;
import java.util.List;

public interface ActivityTypeRepository {
    List<ActivityType> findAll();

    ActivityType findById(Integer id);

    int save(ActivityType activityType);

    int update(ActivityType activityType);

    int delete(Integer id);
}
