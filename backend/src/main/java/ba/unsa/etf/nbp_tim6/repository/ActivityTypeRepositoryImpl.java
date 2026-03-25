package ba.unsa.etf.nbp_tim6.repository;

import ba.unsa.etf.nbp_tim6.model.ActivityType;
import ba.unsa.etf.nbp_tim6.repository.abstraction.ActivityTypeRepository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ActivityTypeRepositoryImpl implements ActivityTypeRepository {

    private final JdbcTemplate jdbcTemplate;

    public ActivityTypeRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<ActivityType> findAll() {
        String sql = "SELECT id, name FROM activity_types";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ActivityType.class));
    }

    @Override
    public ActivityType findById(Integer id) {
        String sql = "SELECT id, name FROM activity_types WHERE id = ?";
        List<ActivityType> results = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ActivityType.class), id);
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public int save(ActivityType activityType) {
        String sql = "INSERT INTO activity_types (name) VALUES (?)";
        return jdbcTemplate.update(sql, activityType.getName());
    }

    @Override
    public int update(ActivityType activityType) {
        String sql = "UPDATE activity_types SET name = ? WHERE id = ?";
        return jdbcTemplate.update(sql, activityType.getName(), activityType.getId());
    }

    @Override
    public int delete(Integer id) {
        String sql = "DELETE FROM activity_types WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
