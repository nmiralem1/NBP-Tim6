package ba.unsa.etf.nbp_tim6.repository;

import ba.unsa.etf.nbp_tim6.model.Address;
import ba.unsa.etf.nbp_tim6.repository.abstraction.AddressRepository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AddressRepositoryImpl implements AddressRepository {

    private final JdbcTemplate jdbcTemplate;

    public AddressRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Address> findAll() {
        String sql = "SELECT id, street, city, postal_code, country, created_at FROM addresses";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Address.class));
    }

    @Override
    public Address findById(Integer id) {
        String sql = "SELECT id, street, city, postal_code, country, created_at FROM addresses WHERE id = ?";
        List<Address> results = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Address.class), id);
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public int save(Address address) {
        String sql = "INSERT INTO addresses (street, city, postal_code, country) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql, address.getStreet(), address.getCity(), address.getPostalCode(),
                address.getCountry());
    }

    @Override
    public int update(Address address) {
        String sql = "UPDATE addresses SET street = ?, city = ?, postal_code = ?, country = ? WHERE id = ?";
        return jdbcTemplate.update(sql, address.getStreet(), address.getCity(), address.getPostalCode(),
                address.getCountry(), address.getId());
    }

    @Override
    public int delete(Integer id) {
        String sql = "DELETE FROM addresses WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
