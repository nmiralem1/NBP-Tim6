package ba.unsa.etf.nbp_tim6.repository;

import ba.unsa.etf.nbp_tim6.model.Activity;
import ba.unsa.etf.nbp_tim6.repository.abstraction.ActivityRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ActivityRepositoryImpl implements ActivityRepository {

    private final JdbcTemplate jdbcTemplate;

    public ActivityRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Activity> activityRowMapper = (rs, rowNum) -> {
        Activity activity = new Activity();
        activity.setId(rs.getInt("id"));
        activity.setTripId(rs.getInt("trip_id"));
        activity.setCityId(rs.getInt("city_id"));
        activity.setActivityTypeId(rs.getInt("activity_type_id"));
        activity.setName(rs.getString("name"));
        activity.setDescription(rs.getString("description"));
        activity.setLocation(rs.getString("location"));
        activity.setActivityDate(
                rs.getDate("activity_date") != null ? rs.getDate("activity_date").toLocalDate() : null);
        activity.setStartTime(
                rs.getTimestamp("start_time") != null ? rs.getTimestamp("start_time").toLocalDateTime().toLocalTime()
                        : null);
        activity.setEndTime(
                rs.getTimestamp("end_time") != null ? rs.getTimestamp("end_time").toLocalDateTime().toLocalTime()
                        : null);
        activity.setPrice(rs.getBigDecimal("price"));
        return activity;
    };

    @Override
    public int save(Activity activity) {
        String sql = """
                    INSERT INTO activities
                    (trip_id, city_id, activity_type_id, name, description, location, activity_date, start_time, end_time, price)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        return jdbcTemplate.update(sql,
                activity.getTripId(),
                activity.getCityId(),
                activity.getActivityTypeId(),
                activity.getName(),
                activity.getDescription(),
                activity.getLocation(),
                activity.getActivityDate(),
                activity.getStartTime(),
                activity.getEndTime(),
                activity.getPrice());
    }

    @Override
    public Activity findById(Integer id) {
        String sql = "SELECT * FROM activities WHERE id = ?";
        List<Activity> results = jdbcTemplate.query(sql, activityRowMapper, id);
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public List<Activity> findByTripId(Integer tripId) {
        String sql = "SELECT * FROM activities WHERE trip_id = ?";
        return jdbcTemplate.query(sql, activityRowMapper, tripId);
    }

    @Override
    public int update(Activity activity) {
        String sql = """
                    UPDATE activities
                    SET trip_id = ?, city_id = ?, activity_type_id = ?, name = ?, description = ?,
                        location = ?, activity_date = ?, start_time = ?, end_time = ?, price = ?
                    WHERE id = ?
                """;
        return jdbcTemplate.update(sql,
                activity.getTripId(),
                activity.getCityId(),
                activity.getActivityTypeId(),
                activity.getName(),
                activity.getDescription(),
                activity.getLocation(),
                activity.getActivityDate(),
                activity.getStartTime(),
                activity.getEndTime(),
                activity.getPrice(),
                activity.getId());
    }

    @Override
    public int delete(Integer id) {
        String sql = "DELETE FROM activities WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
