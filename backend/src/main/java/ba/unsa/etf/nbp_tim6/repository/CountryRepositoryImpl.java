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

    @Override
    public Country findById(Integer id) {
        String sql = "SELECT id, name, code, currency, language FROM countries WHERE id = ?";
        List<Country> results = jdbcTemplate.query(sql, countryRowMapper, id);
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public int save(Country country) {
        String sql = "INSERT INTO countries (name, code, currency, language) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql, country.getName(), country.getCode(), country.getCurrency(),
                country.getLanguage());
    }

    @Override
    public int update(Country country) {
        String sql = "UPDATE countries SET name = ?, code = ?, currency = ?, language = ? WHERE id = ?";
        return jdbcTemplate.update(sql, country.getName(), country.getCode(), country.getCurrency(),
                country.getLanguage(), country.getId());
    }

    @Override
    public int delete(Integer id) {
        String sql = "DELETE FROM countries WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}