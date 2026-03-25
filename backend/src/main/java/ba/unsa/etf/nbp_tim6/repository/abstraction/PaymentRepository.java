package ba.unsa.etf.nbp_tim6.repository.abstraction;

import ba.unsa.etf.nbp_tim6.model.Payment;
import java.util.List;

public interface PaymentRepository {
    List<Payment> findAll();

    Payment findById(Integer id);

    List<Payment> findByTripId(Integer tripId);

    List<Payment> findByUserId(Integer userId);

    int save(Payment payment);

    int update(Payment payment);

    int delete(Integer id);
}
