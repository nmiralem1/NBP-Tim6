package ba.unsa.etf.nbp_tim6.service;

import ba.unsa.etf.nbp_tim6.model.Transport;
import ba.unsa.etf.nbp_tim6.repository.abstraction.TransportRepository;
import ba.unsa.etf.nbp_tim6.service.abstraction.TransportService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransportServiceImpl implements TransportService {

    private final TransportRepository repository;

    public TransportServiceImpl(TransportRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Transport> getAll() {
        return repository.findAll();
    }

    @Override
    public Transport getById(Integer id) {
        Transport transport = repository.findById(id);
        if (transport == null) {
            throw new RuntimeException("Transport not found!");
        }
        return transport;
    }

    @Override
    public List<Transport> getByTripId(Integer tripId) {
        return repository.findByTripId(tripId);
    }

    @Override
    public void create(Transport transport) {
        repository.save(transport);
    }

    @Override
    public void update(Transport transport) {
        repository.update(transport);
    }

    @Override
    public void delete(Integer id) {
        repository.delete(id);
    }
}
