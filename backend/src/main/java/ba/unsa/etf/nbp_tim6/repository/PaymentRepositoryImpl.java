package ba.unsa.etf.nbp_tim6.repository;

import ba.unsa.etf.nbp_tim6.model.Payment;
import ba.unsa.etf.nbp_tim6.repository.abstraction.PaymentRepository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PaymentRepositoryImpl implements PaymentRepository {

    private final JdbcTemplate jdbcTemplate;

    public PaymentRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Payment> findAll() {
        String sql = "SELECT ID, TRIP_ID, BOOKING_ID, USER_ID, PAYMENT_METHOD_ID, AMOUNT, PAYMENT_DATE, PAYMENT_STATUS FROM NBPT6.PAYMENTS";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Payment.class));
    }

    @Override
    public Payment findById(Integer id) {
        String sql = "SELECT ID, TRIP_ID, BOOKING_ID, USER_ID, PAYMENT_METHOD_ID, AMOUNT, PAYMENT_DATE, PAYMENT_STATUS FROM NBPT6.PAYMENTS WHERE ID = ?";
        List<Payment> results = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Payment.class), id);
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public List<Payment> findByTripId(Integer tripId) {
        String sql = "SELECT ID, TRIP_ID, BOOKING_ID, USER_ID, PAYMENT_METHOD_ID, AMOUNT, PAYMENT_DATE, PAYMENT_STATUS FROM NBPT6.PAYMENTS WHERE TRIP_ID = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Payment.class), tripId);
    }

    @Override
    public List<Payment> findByUserId(Integer userId) {
        String sql = "SELECT ID, TRIP_ID, BOOKING_ID, USER_ID, PAYMENT_METHOD_ID, AMOUNT, PAYMENT_DATE, PAYMENT_STATUS FROM NBPT6.PAYMENTS WHERE USER_ID = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Payment.class), userId);
    }

    @Override
    public int save(Payment payment) {
        String sql = "INSERT INTO NBPT6.PAYMENTS (TRIP_ID, BOOKING_ID, USER_ID, PAYMENT_METHOD_ID, AMOUNT, PAYMENT_DATE, PAYMENT_STATUS) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        // Handle default values for read-only fields if they are null
        java.time.LocalDateTime date = (payment.getPaymentDate() == null) ? java.time.LocalDateTime.now() : payment.getPaymentDate();
        String status = (payment.getPaymentStatus() == null) ? "COMPLETED" : payment.getPaymentStatus();
        
        try {
            return jdbcTemplate.update(sql,
                payment.getTripId(),
                payment.getBookingId(),
                payment.getUserId(),
                payment.getPaymentMethodId(),
                payment.getAmount(),
                date,
                status);
        } catch (Exception e) {
            System.err.println("Database error during payment save: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public int update(Payment payment) {
        String sql = "UPDATE NBPT6.PAYMENTS SET TRIP_ID = ?, BOOKING_ID = ?, USER_ID = ?, PAYMENT_METHOD_ID = ?, AMOUNT = ?, PAYMENT_DATE = ?, PAYMENT_STATUS = ? WHERE ID = ?";
        return jdbcTemplate.update(sql,
                payment.getTripId(),
                payment.getBookingId(),
                payment.getUserId(),
                payment.getPaymentMethodId(),
                payment.getAmount(),
                payment.getPaymentDate(),
                payment.getPaymentStatus(),
                payment.getId());
    }

    @Override
    public int delete(Integer id) {
        String sql = "DELETE FROM NBPT6.PAYMENTS WHERE ID = ?";
        return jdbcTemplate.update(sql, id);
    }
}
