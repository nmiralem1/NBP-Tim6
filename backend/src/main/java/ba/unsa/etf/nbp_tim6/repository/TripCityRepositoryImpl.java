package ba.unsa.etf.nbp_tim6.repository;

import ba.unsa.etf.nbp_tim6.model.TripCity;
import ba.unsa.etf.nbp_tim6.repository.abstraction.TripCityRepository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TripCityRepositoryImpl implements TripCityRepository {

    private final JdbcTemplate jdbcTemplate;

    public TripCityRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<TripCity> findAll() {
        String sql = "SELECT id, trip_id, city_id, arrival_date, departure_date, notes FROM trip_cities";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TripCity.class));
    }

    @Override
    public TripCity findById(Integer id) {
        String sql = "SELECT id, trip_id, city_id, arrival_date, departure_date, notes FROM trip_cities WHERE id = ?";
        List<TripCity> results = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TripCity.class), id);
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public List<TripCity> findByTripId(Integer tripId) {
        String sql = "SELECT id, trip_id, city_id, arrival_date, departure_date, notes FROM trip_cities WHERE trip_id = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TripCity.class), tripId);
    }

    @Override
    public int save(TripCity tripCity) {
        String sql = "INSERT INTO trip_cities (trip_id, city_id, arrival_date, departure_date, notes) VALUES (?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, tripCity.getTripId(), tripCity.getCityId(), tripCity.getArrivalDate(),
                tripCity.getDepartureDate(), tripCity.getNotes());
    }

    @Override
    public int update(TripCity tripCity) {
        String sql = "UPDATE trip_cities SET trip_id = ?, city_id = ?, arrival_date = ?, departure_date = ?, notes = ? WHERE id = ?";
        return jdbcTemplate.update(sql, tripCity.getTripId(), tripCity.getCityId(), tripCity.getArrivalDate(),
                tripCity.getDepartureDate(), tripCity.getNotes(), tripCity.getId());
    }

    @Override
    public int delete(Integer id) {
        String sql = "DELETE FROM trip_cities WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
