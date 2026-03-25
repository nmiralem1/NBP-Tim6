package ba.unsa.etf.nbp_tim6.repository;

import ba.unsa.etf.nbp_tim6.model.Country;
import ba.unsa.etf.nbp_tim6.repository.abstraction.CountryRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CountryRepositoryImpl implements CountryRepository {

    private final JdbcTemplate jdbcTemplate;

    public CountryRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Country> countryRowMapper = (rs, rowNum) -> {
        Country country = new Country();
        country.setId(rs.getInt("id"));
        country.setName(rs.getString("name"));
        country.setCode(rs.getString("code"));
        country.setCurrency(rs.getString("currency"));
        country.setLanguage(rs.getString("language"));
        return country;
    };

    @Override
    public List<Country> findAll() {
        String sql = "SELECT id, name, code, currency, language FROM countries";
        return jdbcTemplate.query(sql, countryRowMapper);
    }
}