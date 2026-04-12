package ba.unsa.etf.nbp_tim6.service;

import ba.unsa.etf.nbp_tim6.model.Booking;
import ba.unsa.etf.nbp_tim6.model.User;
import ba.unsa.etf.nbp_tim6.repository.abstraction.AccommodationRepository;
import ba.unsa.etf.nbp_tim6.repository.abstraction.BookingRepository;
import ba.unsa.etf.nbp_tim6.repository.abstraction.UserRepository;
import ba.unsa.etf.nbp_tim6.service.abstraction.BookingService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final AccommodationRepository accommodationRepository;
    private final UserRepository userRepository;

    public BookingServiceImpl(BookingRepository bookingRepository, AccommodationRepository accommodationRepository, UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.accommodationRepository = accommodationRepository;
        this.userRepository = userRepository;
    }

    public void createBooking(Booking booking) {

        // Date validation
        long days = ChronoUnit.DAYS.between(
                booking.getCheckIn(),
                booking.getCheckOut());

        if (days <= 0) {
            throw new RuntimeException("Check-out must be after check-in!");
        }

        // Fetch price per night
        BigDecimal pricePerNight = accommodationRepository.getPricePerNight(booking.getAccommodationId());

        // Calculate total price
        BigDecimal total = pricePerNight.multiply(BigDecimal.valueOf(days));
        booking.setTotalPrice(total);

        // Set default status
        booking.setBookingStatus("pending");

        // Generate booking reference
        booking.setBookingReference(UUID.randomUUID().toString());

        // Save
        bookingRepository.save(booking);
    }

    @Override
    public Booking getBookingById(Integer id) {
        Booking booking = bookingRepository.findById(id);
        if (booking == null) {
            throw new RuntimeException("Booking not found!");
        }
        return booking;
    }

    @Override
    public java.util.List<Booking> getBookingsByUserId(Integer userId) {
        return bookingRepository.findByUserId(userId);
    }

    @Override
    public void updateBooking(Booking booking) {
        bookingRepository.update(booking);
    }

    @Override
    public void deleteBooking(Integer id) {
        bookingRepository.delete(id);
    }

    @Override
    public List<Booking> getBookingsForAuthenticatedUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found!"));
        return bookingRepository.findByUserId(user.getId());
    }
}