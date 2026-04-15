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
        String sql = "SELECT c.id, c.country_id as countryId, c.name, c.postal_code as postalCode, c.description, c.image_url as imageUrl, co.name as countryName, co.continent "
                + "FROM cities c JOIN countries co ON c.country_id = co.id";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(City.class));
    }

    @Override
    public City findById(Integer id) {
        String sql = "SELECT c.id, c.country_id as countryId, c.name, c.postal_code as postalCode, "
                + "c.description, c.image_url as imageUrl, co.name as countryName, co.continent "
                + "FROM cities c JOIN countries co ON c.country_id = co.id WHERE c.id = ?";
        List<City> results = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(City.class), id);
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public List<City> findByCountryId(Integer countryId) {
        String sql = "SELECT c.id, c.country_id as countryId, c.name, c.postal_code as postalCode, "
                + "c.description, c.image_url as imageUrl, co.name as countryName, co.continent "
                + "FROM cities c JOIN countries co ON c.country_id = co.id WHERE c.country_id = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(City.class), countryId);
    }

    @Override
    public int save(City city) {
        String sql = "INSERT INTO cities (country_id, name, postal_code, description, image_url) VALUES (?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, city.getCountryId(), city.getName(), city.getPostalCode(),
                city.getDescription(), city.getImageUrl());
    }

    @Override
    public int update(City city) {
        String sql = "UPDATE cities SET country_id = ?, name = ?, postal_code = ?, description = ?, image_url = ? WHERE id = ?";
        return jdbcTemplate.update(sql, city.getCountryId(), city.getName(), city.getPostalCode(),
                city.getDescription(), city.getImageUrl(), city.getId());
    }

    @Override
    public int delete(Integer id) {
        String sql = "DELETE FROM cities WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
