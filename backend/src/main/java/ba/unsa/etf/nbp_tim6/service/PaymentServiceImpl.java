package ba.unsa.etf.nbp_tim6.service;

import ba.unsa.etf.nbp_tim6.model.Payment;
import ba.unsa.etf.nbp_tim6.repository.abstraction.BookingRepository;
import ba.unsa.etf.nbp_tim6.repository.abstraction.PaymentRepository;
import ba.unsa.etf.nbp_tim6.service.abstraction.PaymentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository repository;
    private final BookingRepository bookingRepository;

    public PaymentServiceImpl(PaymentRepository repository, BookingRepository bookingRepository) {
        this.repository = repository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public List<Payment> getAll() {
        return repository.findAll();
    }

    @Override
    public Payment getById(Integer id) {
        Payment payment = repository.findById(id);
        if (payment == null) {
            throw new RuntimeException("Payment not found!");
        }
        return payment;
    }

    @Override
    public List<Payment> getByTrip(Integer tripId) {
        return repository.findByTripId(tripId);
    }

    @Override
    public List<Payment> getByUser(Integer userId) {
        return repository.findByUserId(userId);
    }

    @Override
    public void create(Payment payment) {
        repository.save(payment);
        if (payment.getBookingId() != null) {
            bookingRepository.updateStatus(payment.getBookingId(), "confirmed");
        }
    }

    @Override
    public void update(Payment payment) {
        repository.update(payment);
    }

    @Override
    public void delete(Integer id) {
        repository.delete(id);
    }
}
