package ba.unsa.etf.nbp_tim6.repository.abstraction;

import ba.unsa.etf.nbp_tim6.model.PaymentMethod;
import java.util.List;

public interface PaymentMethodRepository {
    List<PaymentMethod> findAll();

    PaymentMethod findById(Integer id);

    int save(PaymentMethod paymentMethod);

    int update(PaymentMethod paymentMethod);

    int delete(Integer id);
}
