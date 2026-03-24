package ba.unsa.etf.nbp_tim6.repository;

import ba.unsa.etf.nbp_tim6.model.Accommodation;
import ba.unsa.etf.nbp_tim6.repository.abstraction.AccommodationRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public class AccommodationRepositoryImpl implements AccommodationRepository {

    private final JdbcTemplate jdbcTemplate;

    public AccommodationRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Accommodation> accommodationRowMapper = (rs, rowNum) -> {
        Accommodation accommodation = new Accommodation();
        accommodation.setId(rs.getInt("id"));
        accommodation.setCityId(rs.getInt("city_id"));
        accommodation.setAccommodationTypeId(rs.getInt("accommodation_type_id"));
        accommodation.setName(rs.getString("name"));
        accommodation.setAddress(rs.getString("address"));
        accommodation.setDescription(rs.getString("description"));
        accommodation.setPricePerNight(rs.getBigDecimal("price_per_night"));
        accommodation.setMaxGuests(rs.getInt("max_guests"));
        accommodation.setStars(rs.getInt("stars"));
        accommodation.setPhone(rs.getString("phone"));
        accommodation.setEmail(rs.getString("email"));
        return accommodation;
    };

    public BigDecimal getPricePerNight(Integer accommodationId) {

        String sql = """
            SELECT price_per_night
            FROM accommodations
            WHERE id = ?
        """;

        return jdbcTemplate.queryForObject(sql, BigDecimal.class, accommodationId);
    }

    @Override
    public List<Accommodation> findAll() {
        String sql = """
            SELECT id, city_id, accommodation_type_id, name, address, description,
                   price_per_night, max_guests, stars, phone, email
            FROM accommodations
        """;
        return jdbcTemplate.query(sql, accommodationRowMapper);
    }

    @Override
    public List<Accommodation> findByCityId(Integer cityId) {
        String sql = """
            SELECT id, city_id, accommodation_type_id, name, address, description,
                   price_per_night, max_guests, stars, phone, email
            FROM accommodations
            WHERE city_id = ?
        """;
        return jdbcTemplate.query(sql, accommodationRowMapper, cityId);
    }
}