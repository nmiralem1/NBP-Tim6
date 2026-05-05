package ba.unsa.etf.nbp_tim6.service;

import ba.unsa.etf.nbp_tim6.model.Booking;
import ba.unsa.etf.nbp_tim6.model.Payment;
import ba.unsa.etf.nbp_tim6.model.User;
import ba.unsa.etf.nbp_tim6.repository.abstraction.BookingRepository;
import ba.unsa.etf.nbp_tim6.repository.abstraction.PaymentRepository;
import ba.unsa.etf.nbp_tim6.repository.abstraction.UserRepository;
import ba.unsa.etf.nbp_tim6.service.abstraction.EmailService;
import ba.unsa.etf.nbp_tim6.service.abstraction.PaymentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository repository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    public PaymentServiceImpl(
            PaymentRepository repository,
            BookingRepository bookingRepository,
            UserRepository userRepository,
            EmailService emailService
    ) {
        this.repository = repository;
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
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

            Booking booking = bookingRepository.findById(payment.getBookingId());
            if (booking == null) {
                throw new RuntimeException("Booking not found!");
            }

            User user = userRepository.findById(payment.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found!"));

            String fullName = ((user.getFirstName() != null ? user.getFirstName() : "") +
                    (user.getLastName() != null ? " " + user.getLastName() : "")).trim();

            emailService.sendBookingConfirmation(
                    user.getEmail(),
                    fullName.isBlank() ? user.getUsername() : fullName,
                    String.valueOf(booking.getId()),
                    booking.getTotalPrice() + " EUR"
            );
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
