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
        activity.setId(rs.getInt("ID"));
        activity.setTripId(rs.getInt("TRIP_ID"));
        activity.setCityId(rs.getInt("CITY_ID"));
        activity.setActivityTypeId(rs.getInt("ACTIVITY_TYPE_ID"));
        activity.setName(rs.getString("NAME"));
        activity.setDescription(rs.getString("DESCRIPTION"));
        activity.setLocation(rs.getString("LOCATION"));
        activity.setActivityDate(
                rs.getDate("ACTIVITY_DATE") != null ? rs.getDate("ACTIVITY_DATE").toLocalDate() : null);
        activity.setStartTime(
                rs.getTimestamp("START_TIME") != null ? rs.getTimestamp("START_TIME").toLocalDateTime().toLocalTime()
                        : null);
        activity.setEndTime(
                rs.getTimestamp("END_TIME") != null ? rs.getTimestamp("END_TIME").toLocalDateTime().toLocalTime()
                        : null);
        activity.setPrice(rs.getBigDecimal("PRICE"));
        activity.setImageUrl(rs.getString("IMAGE_URL"));
        return activity;
    };

    @Override
    public int save(Activity activity) {
        String sql = """
                    INSERT INTO NBPT6.ACTIVITIES
                    (TRIP_ID, CITY_ID, ACTIVITY_TYPE_ID, NAME, DESCRIPTION, LOCATION, ACTIVITY_DATE, START_TIME, END_TIME, PRICE, IMAGE_URL)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
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
                activity.getImageUrl());
    }

    @Override
    public Activity findById(Integer id) {
        String sql = "SELECT * FROM NBPT6.ACTIVITIES WHERE ID = ?";
        List<Activity> results = jdbcTemplate.query(sql, activityRowMapper, id);
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public List<Activity> findAll() {
        String sql = "SELECT * FROM NBPT6.ACTIVITIES ORDER BY ID";
        return jdbcTemplate.query(sql, activityRowMapper);
    }

    @Override
    public List<Activity> findByTripId(Integer tripId) {
        String sql = "SELECT * FROM NBPT6.ACTIVITIES WHERE TRIP_ID = ?";
        return jdbcTemplate.query(sql, activityRowMapper, tripId);
    }

    @Override
    public int update(Activity activity) {
        String sql = """
                    UPDATE NBPT6.ACTIVITIES
                    SET TRIP_ID = ?, CITY_ID = ?, ACTIVITY_TYPE_ID = ?, NAME = ?, DESCRIPTION = ?,
                        LOCATION = ?, ACTIVITY_DATE = ?, START_TIME = ?, END_TIME = ?, PRICE = ?, IMAGE_URL = ?
                    WHERE ID = ?
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
                activity.getImageUrl(),
                activity.getId());
    }

    @Override
    public int delete(Integer id) {
        String sql = "DELETE FROM NBPT6.ACTIVITIES WHERE ID = ?";
        return jdbcTemplate.update(sql, id);
    }
}
