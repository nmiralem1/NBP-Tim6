package ba.unsa.etf.nbp_tim6.service;

import ba.unsa.etf.nbp_tim6.model.ActivityType;
import ba.unsa.etf.nbp_tim6.repository.abstraction.ActivityTypeRepository;
import ba.unsa.etf.nbp_tim6.service.abstraction.ActivityTypeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityTypeServiceImpl implements ActivityTypeService {

    private final ActivityTypeRepository repository;

    public ActivityTypeServiceImpl(ActivityTypeRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ActivityType> getAll() {
        return repository.findAll();
    }

    @Override
    public ActivityType getById(Integer id) {
        ActivityType type = repository.findById(id);
        if (type == null) {
            throw new RuntimeException("Activity type not found!");
        }
        return type;
    }

    @Override
    public void create(ActivityType type) {
        repository.save(type);
    }

    @Override
    public void update(ActivityType type) {
        repository.update(type);
    }

    @Override
    public void delete(Integer id) {
        repository.delete(id);
    }
}
