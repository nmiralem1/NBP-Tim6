package ba.unsa.etf.nbp_tim6.service.abstraction;

import ba.unsa.etf.nbp_tim6.model.ActivityType;
import java.util.List;

public interface ActivityTypeService {
    List<ActivityType> getAll();

    ActivityType getById(Integer id);

    void create(ActivityType type);

    void update(ActivityType type);

    void delete(Integer id);
}
