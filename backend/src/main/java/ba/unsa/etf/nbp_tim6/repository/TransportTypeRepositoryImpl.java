package ba.unsa.etf.nbp_tim6.repository;

import ba.unsa.etf.nbp_tim6.model.TransportType;
import ba.unsa.etf.nbp_tim6.repository.abstraction.TransportTypeRepository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TransportTypeRepositoryImpl implements TransportTypeRepository {

    private final JdbcTemplate jdbcTemplate;

    public TransportTypeRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<TransportType> findAll() {
        String sql = "SELECT id, name FROM transport_types";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TransportType.class));
    }

    @Override
    public TransportType findById(Integer id) {
        String sql = "SELECT id, name FROM transport_types WHERE id = ?";
        List<TransportType> results = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TransportType.class), id);
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public int save(TransportType transportType) {
        String sql = "INSERT INTO transport_types (name) VALUES (?)";
        return jdbcTemplate.update(sql, transportType.getName());
    }

    @Override
    public int update(TransportType transportType) {
        String sql = "UPDATE transport_types SET name = ? WHERE id = ?";
        return jdbcTemplate.update(sql, transportType.getName(), transportType.getId());
    }

    @Override
    public int delete(Integer id) {
        String sql = "DELETE FROM transport_types WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
