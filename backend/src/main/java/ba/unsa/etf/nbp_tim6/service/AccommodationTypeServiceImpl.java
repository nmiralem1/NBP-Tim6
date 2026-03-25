package ba.unsa.etf.nbp_tim6.service;

import ba.unsa.etf.nbp_tim6.model.AccommodationType;
import ba.unsa.etf.nbp_tim6.repository.abstraction.AccommodationTypeRepository;
import ba.unsa.etf.nbp_tim6.service.abstraction.AccommodationTypeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccommodationTypeServiceImpl implements AccommodationTypeService {

    private final AccommodationTypeRepository repository;

    public AccommodationTypeServiceImpl(AccommodationTypeRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<AccommodationType> getAll() {
        return repository.findAll();
    }

    @Override
    public AccommodationType getById(Integer id) {
        AccommodationType type = repository.findById(id);
        if (type == null) {
            throw new RuntimeException("Accommodation type not found!");
        }
        return type;
    }

    @Override
    public void create(AccommodationType type) {
        repository.save(type);
    }

    @Override
    public void update(AccommodationType type) {
        repository.update(type);
    }

    @Override
    public void delete(Integer id) {
        repository.delete(id);
    }
}
