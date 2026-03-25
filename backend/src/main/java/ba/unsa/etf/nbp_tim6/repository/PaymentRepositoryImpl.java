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
        String sql = "SELECT id, trip_id, booking_id, user_id, payment_method_id, amount, payment_date, payment_status FROM payments";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Payment.class));
    }

    @Override
    public Payment findById(Integer id) {
        String sql = "SELECT id, trip_id, booking_id, user_id, payment_method_id, amount, payment_date, payment_status FROM payments WHERE id = ?";
        List<Payment> results = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Payment.class), id);
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public List<Payment> findByTripId(Integer tripId) {
        String sql = "SELECT id, trip_id, booking_id, user_id, payment_method_id, amount, payment_date, payment_status FROM payments WHERE trip_id = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Payment.class), tripId);
    }

    @Override
    public List<Payment> findByUserId(Integer userId) {
        String sql = "SELECT id, trip_id, booking_id, user_id, payment_method_id, amount, payment_date, payment_status FROM payments WHERE user_id = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Payment.class), userId);
    }

    @Override
    public int save(Payment payment) {
        String sql = "INSERT INTO payments (trip_id, booking_id, user_id, payment_method_id, amount, payment_date, payment_status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                payment.getTripId(),
                payment.getBookingId(),
                payment.getUserId(),
                payment.getPaymentMethodId(),
                payment.getAmount(),
                payment.getPaymentDate(),
                payment.getPaymentStatus());
    }

    @Override
    public int update(Payment payment) {
        String sql = "UPDATE payments SET trip_id = ?, booking_id = ?, user_id = ?, payment_method_id = ?, amount = ?, payment_date = ?, payment_status = ? WHERE id = ?";
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
        String sql = "DELETE FROM payments WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
