package ba.unsa.etf.nbp_tim6.repository;

import ba.unsa.etf.nbp_tim6.model.TripActivity;
import ba.unsa.etf.nbp_tim6.repository.abstraction.TripActivityRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class TripActivityRepositoryImpl implements TripActivityRepository {

    private final JdbcTemplate jdbcTemplate;

    public TripActivityRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<TripActivity> rowMapper = (rs, rowNum) -> {
        TripActivity ta = new TripActivity();
        ta.setId(rs.getInt("ID"));
        ta.setTripId(rs.getInt("TRIP_ID"));
        ta.setActivityId(rs.getInt("ACTIVITY_ID"));
        ta.setAddedAt(rs.getTimestamp("ADDED_AT").toLocalDateTime());
        return ta;
    };

    @Override
    public int save(TripActivity tripActivity) {
        String sql = "INSERT INTO NBPT6.TRIP_ACTIVITIES (TRIP_ID, ACTIVITY_ID) VALUES (?, ?)";
        return jdbcTemplate.update(sql, tripActivity.getTripId(), tripActivity.getActivityId());
    }

    @Override
    public List<TripActivity> findByTripId(Integer tripId) {
        String sql = "SELECT * FROM NBPT6.TRIP_ACTIVITIES WHERE TRIP_ID = ?";
        return jdbcTemplate.query(sql, rowMapper, tripId);
    }

    @Override
    public int delete(Integer id) {
        String sql = "DELETE FROM NBPT6.TRIP_ACTIVITIES WHERE ID = ?";
        return jdbcTemplate.update(sql, id);
    }
}
