package ba.unsa.etf.nbp_tim6.service.abstraction;

import ba.unsa.etf.nbp_tim6.model.PaymentMethod;
import java.util.List;

public interface PaymentMethodService {
    List<PaymentMethod> getAll();

    PaymentMethod getById(Integer id);

    void create(PaymentMethod method);

    void update(PaymentMethod method);

    void delete(Integer id);
}
