package ba.unsa.etf.nbp_tim6.repository;

import ba.unsa.etf.nbp_tim6.model.City;
import ba.unsa.etf.nbp_tim6.repository.abstraction.CityRepository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CityRepositoryImpl implements CityRepository {

    private final JdbcTemplate jdbcTemplate;

    public CityRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<City> findAll() {
        String sql = "SELECT id, country_id, name, postal_code, description FROM cities";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(City.class));
    }

    @Override
    public City findById(Integer id) {
        String sql = "SELECT id, country_id, name, postal_code, description FROM cities WHERE id = ?";
        List<City> results = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(City.class), id);
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public List<City> findByCountryId(Integer countryId) {
        String sql = "SELECT id, country_id, name, postal_code, description FROM cities WHERE country_id = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(City.class), countryId);
    }

    @Override
    public int save(City city) {
        String sql = "INSERT INTO cities (country_id, name, postal_code, description) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql, city.getCountryId(), city.getName(), city.getPostalCode(),
                city.getDescription());
    }

    @Override
    public int update(City city) {
        String sql = "UPDATE cities SET country_id = ?, name = ?, postal_code = ?, description = ? WHERE id = ?";
        return jdbcTemplate.update(sql, city.getCountryId(), city.getName(), city.getPostalCode(),
                city.getDescription(), city.getId());
    }

    @Override
    public int delete(Integer id) {
        String sql = "DELETE FROM cities WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
