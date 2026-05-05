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
        String sql = "SELECT ID, USER_ID as userId, TITLE, DESCRIPTION, START_DATE as startDate, END_DATE as endDate, BUDGET, STATUS, IMAGE_URL as imageUrl, CREATED_AT as createdAt FROM NBPT6.TRIPS";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Trip.class));
    }

    public int save(Trip trip) {
        String sql = """
                    INSERT INTO NBPT6.TRIPS
                    (USER_ID, TITLE, DESCRIPTION, START_DATE, END_DATE, BUDGET, STATUS, IMAGE_URL)
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
        String sql = "SELECT * FROM NBPT6.TRIPS WHERE ID = ?";
        List<Trip> trips = jdbcTemplate.query(
                sql,
                new BeanPropertyRowMapper<>(Trip.class),
                id);
        return trips.isEmpty() ? null : trips.get(0);
    }

    public int update(Trip trip) {
        String sql = """
                    UPDATE NBPT6.TRIPS
                    SET TITLE = ?, DESCRIPTION = ?, START_DATE = ?, END_DATE = ?, BUDGET = ?, STATUS = ?, IMAGE_URL = ?
                    WHERE ID = ?
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
        String sql = "DELETE FROM NBPT6.TRIPS WHERE ID = ?";
        return jdbcTemplate.update(sql, id);
    }

    public List<Trip> findByUserId(Integer userId) {
        String sql = "SELECT * FROM NBPT6.TRIPS WHERE USER_ID = ?";

        return jdbcTemplate.query(
                sql,
                new BeanPropertyRowMapper<>(Trip.class),
                userId);
    }
}