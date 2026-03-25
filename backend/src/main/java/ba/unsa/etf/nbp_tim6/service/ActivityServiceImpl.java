package ba.unsa.etf.nbp_tim6.service;

import ba.unsa.etf.nbp_tim6.model.Activity;
import ba.unsa.etf.nbp_tim6.repository.abstraction.ActivityRepository;
import ba.unsa.etf.nbp_tim6.service.abstraction.ActivityService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityServiceImpl implements ActivityService {

    private final ActivityRepository activityRepository;

    public ActivityServiceImpl(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    @Override
    public void createActivity(Activity activity) {
        activityRepository.save(activity);
    }

    @Override
    public Activity getActivityById(Integer id) {
        Activity activity = activityRepository.findById(id);
        if (activity == null) {
            throw new RuntimeException("Activity not found!");
        }
        return activity;
    }

    @Override
    public List<Activity> getActivitiesByTripId(Integer tripId) {
        return activityRepository.findByTripId(tripId);
    }

    @Override
    public void updateActivity(Activity activity) {
        activityRepository.update(activity);
    }

    @Override
    public void deleteActivity(Integer id) {
        activityRepository.delete(id);
    }
}
