package ba.unsa.etf.nbp_tim6.repository;

import ba.unsa.etf.nbp_tim6.model.PaymentMethod;
import ba.unsa.etf.nbp_tim6.repository.abstraction.PaymentMethodRepository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PaymentMethodRepositoryImpl implements PaymentMethodRepository {

    private final JdbcTemplate jdbcTemplate;

    public PaymentMethodRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<PaymentMethod> findAll() {
        String sql = "SELECT id, name FROM payment_methods";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(PaymentMethod.class));
    }

    @Override
    public PaymentMethod findById(Integer id) {
        String sql = "SELECT id, name FROM payment_methods WHERE id = ?";
        List<PaymentMethod> results = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(PaymentMethod.class), id);
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public int save(PaymentMethod paymentMethod) {
        String sql = "INSERT INTO payment_methods (name) VALUES (?)";
        return jdbcTemplate.update(sql, paymentMethod.getName());
    }

    @Override
    public int update(PaymentMethod paymentMethod) {
        String sql = "UPDATE payment_methods SET name = ? WHERE id = ?";
        return jdbcTemplate.update(sql, paymentMethod.getName(), paymentMethod.getId());
    }

    @Override
    public int delete(Integer id) {
        String sql = "DELETE FROM payment_methods WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
