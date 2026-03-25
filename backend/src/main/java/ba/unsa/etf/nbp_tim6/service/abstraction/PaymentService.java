package ba.unsa.etf.nbp_tim6.service.abstraction;

import ba.unsa.etf.nbp_tim6.model.Payment;
import java.util.List;

public interface PaymentService {
    List<Payment> getAll();

    Payment getById(Integer id);

    List<Payment> getByTrip(Integer tripId);

    List<Payment> getByUser(Integer userId);

    void create(Payment payment);

    void update(Payment payment);

    void delete(Integer id);
}
