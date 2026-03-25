package ba.unsa.etf.nbp_tim6.service;

import ba.unsa.etf.nbp_tim6.model.TransportType;
import ba.unsa.etf.nbp_tim6.repository.abstraction.TransportTypeRepository;
import ba.unsa.etf.nbp_tim6.service.abstraction.TransportTypeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransportTypeServiceImpl implements TransportTypeService {

    private final TransportTypeRepository repository;

    public TransportTypeServiceImpl(TransportTypeRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<TransportType> getAll() {
        return repository.findAll();
    }

    @Override
    public TransportType getById(Integer id) {
        TransportType type = repository.findById(id);
        if (type == null) {
            throw new RuntimeException("Transport type not found!");
        }
        return type;
    }

    @Override
    public void create(TransportType type) {
        repository.save(type);
    }

    @Override
    public void update(TransportType type) {
        repository.update(type);
    }

    @Override
    public void delete(Integer id) {
        repository.delete(id);
    }
}
