package ba.unsa.etf.nbp_tim6.repository;

import ba.unsa.etf.nbp_tim6.repository.abstraction.AccommodationRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public class AccommodationRepositoryImpl implements AccommodationRepository {

    private final JdbcTemplate jdbcTemplate;

    public AccommodationRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public BigDecimal getPricePerNight(Integer accommodationId) {

        String sql = """
            SELECT price_per_night
            FROM accommodations
            WHERE id = ?
        """;

        return jdbcTemplate.queryForObject(sql, BigDecimal.class, accommodationId);
    }
}