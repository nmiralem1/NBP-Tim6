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

    private final org.springframework.jdbc.core.RowMapper<Booking> bookingRowMapper = (rs, rowNum) -> {
        Booking booking = new Booking();
        booking.setId(rs.getInt("id"));
        booking.setTripId(rs.getInt("trip_id"));
        booking.setUserId(rs.getInt("user_id"));
        booking.setAccommodationId(rs.getInt("accommodation_id"));
        booking.setCheckIn(rs.getDate("check_in").toLocalDate());
        booking.setCheckOut(rs.getDate("check_out").toLocalDate());
        booking.setGuestsCount(rs.getInt("guests_count"));
        booking.setTotalPrice(rs.getBigDecimal("total_price"));
        booking.setBookingStatus(rs.getString("booking_status"));
        booking.setBookingReference(rs.getString("booking_reference"));
        booking.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return booking;
    };

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
                booking.getBookingReference());
    }

    public Booking findById(Integer id) {
        String sql = "SELECT * FROM bookings WHERE id = ?";
        java.util.List<Booking> results = jdbcTemplate.query(sql, bookingRowMapper, id);
        return results.isEmpty() ? null : results.get(0);
    }

    public java.util.List<Booking> findByUserId(Integer userId) {
        String sql = "SELECT * FROM bookings WHERE user_id = ?";
        return jdbcTemplate.query(sql, bookingRowMapper, userId);
    }

    public int update(Booking booking) {
        String sql = """
                    UPDATE bookings
                    SET trip_id = ?, user_id = ?, accommodation_id = ?, check_in = ?, check_out = ?,
                        guests_count = ?, total_price = ?, booking_status = ?, booking_reference = ?
                    WHERE id = ?
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
                booking.getBookingReference(),
                booking.getId());
    }

    public int delete(Integer id) {
        String sql = "DELETE FROM bookings WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}