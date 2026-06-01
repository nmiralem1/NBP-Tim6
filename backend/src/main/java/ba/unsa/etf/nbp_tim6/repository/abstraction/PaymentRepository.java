package ba.unsa.etf.nbp_tim6.repository.abstraction;

import ba.unsa.etf.nbp_tim6.model.Payment;
import java.util.List;

public interface PaymentRepository {
    List<Payment> findAll();

    Payment findById(Integer id);

    Payment findByBookingId(Integer bookingId);

    List<Payment> findByTripId(Integer tripId);

    List<Payment> findByUserId(Integer userId);

    String exportPaymentsAsXml();

    int save(Payment payment);

    int update(Payment payment);

    int delete(Integer id);
}
