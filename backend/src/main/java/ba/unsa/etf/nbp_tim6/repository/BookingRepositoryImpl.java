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
        booking.setId(rs.getInt("ID"));
        booking.setTripId(rs.getInt("TRIP_ID"));
        booking.setUserId(rs.getInt("USER_ID"));
        booking.setAccommodationId(rs.getInt("ACCOMMODATION_ID"));
        booking.setCheckIn(rs.getDate("CHECK_IN").toLocalDate());
        booking.setCheckOut(rs.getDate("CHECK_OUT").toLocalDate());
        booking.setGuestsCount(rs.getInt("GUESTS_COUNT"));
        booking.setTotalPrice(rs.getBigDecimal("TOTAL_PRICE"));
        booking.setBookingStatus(rs.getString("BOOKING_STATUS"));
        booking.setBookingReference(rs.getString("BOOKING_REFERENCE"));
        booking.setCreatedAt(rs.getTimestamp("CREATED_AT").toLocalDateTime());
        return booking;
    };

    public int save(Booking booking) {

        String sql = """
                    INSERT INTO NBPT6.BOOKINGS
                    (TRIP_ID, USER_ID, ACCOMMODATION_ID, CHECK_IN, CHECK_OUT,
                     GUESTS_COUNT, TOTAL_PRICE, BOOKING_STATUS, BOOKING_REFERENCE, CREATED_AT)
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
        String sql = "SELECT * FROM NBPT6.BOOKINGS WHERE ID = ?";
        java.util.List<Booking> results = jdbcTemplate.query(sql, bookingRowMapper, id);
        return results.isEmpty() ? null : results.get(0);
    }

    public java.util.List<Booking> findByUserId(Integer userId) {
        String sql = "SELECT * FROM NBPT6.BOOKINGS WHERE USER_ID = ?";
        return jdbcTemplate.query(sql, bookingRowMapper, userId);
    }

    public int update(Booking booking) {
        String sql = """
                    UPDATE NBPT6.BOOKINGS
                    SET TRIP_ID = ?, USER_ID = ?, ACCOMMODATION_ID = ?, CHECK_IN = ?, CHECK_OUT = ?,
                        GUESTS_COUNT = ?, TOTAL_PRICE = ?, BOOKING_STATUS = ?, BOOKING_REFERENCE = ?
                    WHERE ID = ?
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
        String sql = "DELETE FROM NBPT6.BOOKINGS WHERE ID = ?";
        return jdbcTemplate.update(sql, id);
    }

    public Integer saveAndReturnId(Booking booking) {
        String sql = """
                    INSERT INTO NBPT6.BOOKINGS
                    (TRIP_ID, USER_ID, ACCOMMODATION_ID, CHECK_IN, CHECK_OUT,
                     GUESTS_COUNT, TOTAL_PRICE, BOOKING_STATUS, BOOKING_REFERENCE, CREATED_AT)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)
                """;

        jdbcTemplate.update(sql,
                booking.getTripId(),
                booking.getUserId(),
                booking.getAccommodationId(),
                booking.getCheckIn(),
                booking.getCheckOut(),
                booking.getGuestsCount(),
                booking.getTotalPrice(),
                booking.getBookingStatus(),
                booking.getBookingReference());

        String fetchSql = "SELECT ID FROM NBPT6.BOOKINGS WHERE BOOKING_REFERENCE = ?";
        return jdbcTemplate.queryForObject(fetchSql, Integer.class, booking.getBookingReference());
    }

    public int updateStatus(Integer id, String status) {
        String sql = "UPDATE NBPT6.BOOKINGS SET BOOKING_STATUS = ? WHERE ID = ?";
        return jdbcTemplate.update(sql, status, id);
    }
}