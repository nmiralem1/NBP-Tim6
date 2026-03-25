package ba.unsa.etf.nbp_tim6.service;

import ba.unsa.etf.nbp_tim6.model.PaymentMethod;
import ba.unsa.etf.nbp_tim6.repository.abstraction.PaymentMethodRepository;
import ba.unsa.etf.nbp_tim6.service.abstraction.PaymentMethodService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentMethodServiceImpl implements PaymentMethodService {

    private final PaymentMethodRepository repository;

    public PaymentMethodServiceImpl(PaymentMethodRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<PaymentMethod> getAll() {
        return repository.findAll();
    }

    @Override
    public PaymentMethod getById(Integer id) {
        PaymentMethod method = repository.findById(id);
        if (method == null) {
            throw new RuntimeException("Payment method not found!");
        }
        return method;
    }

    @Override
    public void create(PaymentMethod method) {
        repository.save(method);
    }

    @Override
    public void update(PaymentMethod method) {
        repository.update(method);
    }

    @Override
    public void delete(Integer id) {
        repository.delete(id);
    }
}
