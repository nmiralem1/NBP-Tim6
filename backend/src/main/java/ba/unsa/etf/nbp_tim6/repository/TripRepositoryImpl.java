package ba.unsa.etf.nbp_tim6.repository;

import ba.unsa.etf.nbp_tim6.model.Trip;
import ba.unsa.etf.nbp_tim6.repository.abstraction.TripRepository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Repository
public class TripRepositoryImpl implements TripRepository {

    private final JdbcTemplate jdbcTemplate;

    public TripRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // INSERT
    public int save(Trip trip) {
        String sql = """
            INSERT INTO trips 
            (user_id, title, description, start_date, end_date, budget, status)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        return jdbcTemplate.update(sql,
                trip.getUserId(),
                trip.getTitle(),
                trip.getDescription(),
                trip.getStartDate(),
                trip.getEndDate(),
                trip.getBudget(),
                "planned"
        );
    }

    // SELECT
    public List<Trip> findByUserId(Integer userId) {
        String sql = "SELECT * FROM trips WHERE user_id = ?";

        return jdbcTemplate.query(
                sql,
                new BeanPropertyRowMapper<>(Trip.class),
                userId
        );
    }
}