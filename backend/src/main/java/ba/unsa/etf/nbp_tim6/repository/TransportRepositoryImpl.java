package ba.unsa.etf.nbp_tim6.repository;

import ba.unsa.etf.nbp_tim6.model.Transport;
import ba.unsa.etf.nbp_tim6.repository.abstraction.TransportRepository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TransportRepositoryImpl implements TransportRepository {

    private final JdbcTemplate jdbcTemplate;

    public TransportRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Transport> findAll() {
        String sql = "SELECT id, trip_id, from_city_id, to_city_id, transport_type_id, company_name, departure_time, arrival_time, ticket_price, seat_number, booking_reference FROM transport";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Transport.class));
    }

    @Override
    public Transport findById(Integer id) {
        String sql = "SELECT id, trip_id, from_city_id, to_city_id, transport_type_id, company_name, departure_time, arrival_time, ticket_price, seat_number, booking_reference FROM transport WHERE id = ?";
        List<Transport> results = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Transport.class), id);
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public List<Transport> findByTripId(Integer tripId) {
        String sql = "SELECT id, trip_id, from_city_id, to_city_id, transport_type_id, company_name, departure_time, arrival_time, ticket_price, seat_number, booking_reference FROM transport WHERE trip_id = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Transport.class), tripId);
    }

    @Override
    public int save(Transport transport) {
        String sql = """
                    INSERT INTO transport (trip_id, from_city_id, to_city_id, transport_type_id, company_name, departure_time, arrival_time, ticket_price, seat_number, booking_reference)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        return jdbcTemplate.update(sql,
                transport.getTripId(),
                transport.getFromCityId(),
                transport.getToCityId(),
                transport.getTransportTypeId(),
                transport.getCompanyName(),
                transport.getDepartureTime(),
                transport.getArrivalTime(),
                transport.getTicketPrice(),
                transport.getSeatNumber(),
                transport.getBookingReference());
    }

    @Override
    public int update(Transport transport) {
        String sql = """
                    UPDATE transport
                    SET trip_id = ?, from_city_id = ?, to_city_id = ?, transport_type_id = ?, company_name = ?, departure_time = ?, arrival_time = ?, ticket_price = ?, seat_number = ?, booking_reference = ?
                    WHERE id = ?
                """;
        return jdbcTemplate.update(sql,
                transport.getTripId(),
                transport.getFromCityId(),
                transport.getToCityId(),
                transport.getTransportTypeId(),
                transport.getCompanyName(),
                transport.getDepartureTime(),
                transport.getArrivalTime(),
                transport.getTicketPrice(),
                transport.getSeatNumber(),
                transport.getBookingReference(),
                transport.getId());
    }

    @Override
    public int delete(Integer id) {
        String sql = "DELETE FROM transport WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
