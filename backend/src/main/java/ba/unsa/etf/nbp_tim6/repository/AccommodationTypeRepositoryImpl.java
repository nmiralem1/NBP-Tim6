package ba.unsa.etf.nbp_tim6.repository;

import ba.unsa.etf.nbp_tim6.model.AccommodationType;
import ba.unsa.etf.nbp_tim6.repository.abstraction.AccommodationTypeRepository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AccommodationTypeRepositoryImpl implements AccommodationTypeRepository {

    private final JdbcTemplate jdbcTemplate;

    public AccommodationTypeRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<AccommodationType> findAll() {
        String sql = "SELECT id, name FROM accommodation_types";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(AccommodationType.class));
    }

    @Override
    public AccommodationType findById(Integer id) {
        String sql = "SELECT id, name FROM accommodation_types WHERE id = ?";
        List<AccommodationType> results = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(AccommodationType.class),
                id);
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public int save(AccommodationType accommodationType) {
        String sql = "INSERT INTO accommodation_types (name) VALUES (?)";
        return jdbcTemplate.update(sql, accommodationType.getName());
    }

    @Override
    public int update(AccommodationType accommodationType) {
        String sql = "UPDATE accommodation_types SET name = ? WHERE id = ?";
        return jdbcTemplate.update(sql, accommodationType.getName(), accommodationType.getId());
    }

    @Override
    public int delete(Integer id) {
        String sql = "DELETE FROM accommodation_types WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
