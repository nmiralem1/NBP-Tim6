package ba.unsa.etf.nbp_tim6.repository;

import ba.unsa.etf.nbp_tim6.model.Booking;
import ba.unsa.etf.nbp_tim6.repository.abstraction.BookingRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BookingRepositoryImpl implements BookingRepository {

    private final JdbcTemplate jdbcTemplate;

    public BookingRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int save(Booking booking) {

        String sql = """
            INSERT INTO bookings
            (trip_id, user_id, accommodation_id, check_in, check_out,
             guests_count, total_price, booking_status, booking_reference, created_at)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)
        """;

        return jdbcTemplate.update(sql,
                booking.getTripId(),
                booking.getUserId(),
                booking.getAccommodationId(),
                booking.getCheckIn(),
                booking.getCheckOut(),
                booking.getGuestsCount(),
                booking.getTotalPrice(),
                booking.getBookingStatus(),
                booking.getBookingReference()
        );
    }
}