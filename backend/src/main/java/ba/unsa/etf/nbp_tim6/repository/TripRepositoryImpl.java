package ba.unsa.etf.nbp_tim6.repository;

import ba.unsa.etf.nbp_tim6.model.Trip;
import ba.unsa.etf.nbp_tim6.repository.abstraction.TripRepository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TripRepositoryImpl implements TripRepository {

    private final JdbcTemplate jdbcTemplate;

    public TripRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Trip> findAll() {
        String sql = "SELECT id, user_id as userId, title, description, start_date as startDate, end_date as endDate, budget, status, image_url as imageUrl, created_at as createdAt FROM trips";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Trip.class));
    }

    public int save(Trip trip) {
        String sql = """
                    INSERT INTO trips
                    (user_id, title, description, start_date, end_date, budget, status, image_url)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        return jdbcTemplate.update(sql,
                trip.getUserId(),
                trip.getTitle(),
                trip.getDescription(),
                trip.getStartDate(),
                trip.getEndDate(),
                trip.getBudget(),
                trip.getStatus() != null ? trip.getStatus() : "planned",
                trip.getImageUrl());
    }

    public Trip findById(Integer id) {
        String sql = "SELECT * FROM trips WHERE id = ?";
        List<Trip> trips = jdbcTemplate.query(
                sql,
                new BeanPropertyRowMapper<>(Trip.class),
                id);
        return trips.isEmpty() ? null : trips.get(0);
    }

    public int update(Trip trip) {
        String sql = """
                    UPDATE trips
                    SET title = ?, description = ?, start_date = ?, end_date = ?, budget = ?, status = ?, image_url = ?
                    WHERE id = ?
                """;

        return jdbcTemplate.update(sql,
                trip.getTitle(),
                trip.getDescription(),
                trip.getStartDate(),
                trip.getEndDate(),
                trip.getBudget(),
                trip.getStatus(),
                trip.getImageUrl(),
                trip.getId());
    }

    public int delete(Integer id) {
        String sql = "DELETE FROM trips WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public List<Trip> findByUserId(Integer userId) {
        String sql = "SELECT * FROM trips WHERE user_id = ?";

        return jdbcTemplate.query(
                sql,
                new BeanPropertyRowMapper<>(Trip.class),
                userId);
    }
}