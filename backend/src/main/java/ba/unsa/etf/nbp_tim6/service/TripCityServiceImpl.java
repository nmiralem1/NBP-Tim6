package ba.unsa.etf.nbp_tim6.service;

import ba.unsa.etf.nbp_tim6.model.TripCity;
import ba.unsa.etf.nbp_tim6.repository.abstraction.TripCityRepository;
import ba.unsa.etf.nbp_tim6.service.abstraction.TripCityService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TripCityServiceImpl implements TripCityService {

    private final TripCityRepository repository;

    public TripCityServiceImpl(TripCityRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<TripCity> getAll() {
        return repository.findAll();
    }

    @Override
    public TripCity getById(Integer id) {
        TripCity tripCity = repository.findById(id);
        if (tripCity == null) {
            throw new RuntimeException("Trip city connectionnot found!");
        }
        return tripCity;
    }

    @Override
    public List<TripCity> getByTripId(Integer tripId) {
        return repository.findByTripId(tripId);
    }

    @Override
    public void create(TripCity tripCity) {
        repository.save(tripCity);
    }

    @Override
    public void update(TripCity tripCity) {
        repository.update(tripCity);
    }

    @Override
    public void delete(Integer id) {
        repository.delete(id);
    }
}
