package ba.unsa.etf.nbp_tim6.service;

import ba.unsa.etf.nbp_tim6.model.Booking;
import ba.unsa.etf.nbp_tim6.repository.abstraction.AccommodationRepository;
import ba.unsa.etf.nbp_tim6.repository.abstraction.BookingRepository;
import ba.unsa.etf.nbp_tim6.service.abstraction.BookingService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final AccommodationRepository accommodationRepository;

    public BookingServiceImpl(BookingRepository bookingRepository, AccommodationRepository accommodationRepository) {
        this.bookingRepository = bookingRepository;
        this.accommodationRepository = accommodationRepository;
    }

    public void createBooking(Booking booking) {

        // 1. VALIDACIJA DATUMA
        long days = ChronoUnit.DAYS.between(
                booking.getCheckIn(),
                booking.getCheckOut()
        );

        if (days <= 0) {
            throw new RuntimeException("Check-out mora biti nakon check-in!");
        }

        // 2. UZMI CIJENU
        BigDecimal pricePerNight = accommodationRepository.getPricePerNight(booking.getAccommodationId());

        // 3. IZRAČUNAJ UKUPNO
        BigDecimal total = pricePerNight.multiply(BigDecimal.valueOf(days));
        booking.setTotalPrice(total);

        // 4. STATUS
        booking.setBookingStatus("pending");

        // 5. GENERIŠI REFERENCU
        booking.setBookingReference(UUID.randomUUID().toString());

        // 6. SAVE
        bookingRepository.save(booking);
    }
}