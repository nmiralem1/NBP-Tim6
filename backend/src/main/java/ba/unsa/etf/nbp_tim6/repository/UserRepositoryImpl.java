package ba.unsa.etf.nbp_tim6.repository;

import ba.unsa.etf.nbp_tim6.model.User;
import ba.unsa.etf.nbp_tim6.repository.abstraction.UserRepository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<User> findById(Integer id) {
        String sql = "SELECT u.id, u.first_name, u.last_name, u.username, u.email, u.password as password_hash, u.phone_number as phone, r.name as role "
                +
                "FROM nbp.nbp_user u " +
                "LEFT JOIN nbp.nbp_role r ON u.role_id = r.id " +
                "WHERE u.id = ?";
        List<User> users = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class), id);
        return users.stream().findFirst();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        String sql = "SELECT u.id, u.FIRST_NAME, u.LAST_NAME, u.USERNAME, u.EMAIL, u.PASSWORD as password_hash, u.PHONE_NUMBER as phone, r.name as role "
                +
                "FROM nbp.nbp_user u " +
                "LEFT JOIN nbp.nbp_role r ON u.role_id = r.id " +
                "WHERE u.username = ?";
        List<User> users = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class), username);
        return users.stream().findFirst();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        String sql = "SELECT u.id, u.FIRST_NAME, u.LAST_NAME, u.USERNAME, u.EMAIL, u.PASSWORD as password_hash, u.PHONE_NUMBER as phone, r.name as role "
                +
                "FROM nbp.nbp_user u " +
                "LEFT JOIN nbp.nbp_role r ON u.role_id = r.id " +
                "WHERE u.email = ?";
        List<User> users = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class), email);
        return users.stream().findFirst();
    }

    @Override
    public boolean existsByUsername(String username) {
        String sql = "SELECT count(*) FROM nbp.nbp_user WHERE username = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, username);
        return count != null && count > 0;
    }

    @Override
    public boolean existsByEmail(String email) {
        String sql = "SELECT count(*) FROM nbp.nbp_user WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            String sql = "INSERT INTO nbp.nbp_user (first_name, last_name, username, email, password, phone_number, role_id) "
                    +
                    "VALUES (?, ?, ?, ?, ?, ?, (SELECT id FROM nbp.nbp_role WHERE name = ?))";
            jdbcTemplate.update(sql, user.getFirstName(), user.getLastName(), user.getUsername(), user.getEmail(),
                    user.getPasswordHash(), user.getPhone(),
                    user.getRole() != null ? user.getRole() : "REGISTERED_USER");
            return findByUsername(user.getUsername()).orElseThrow();
        } else {
            String sql = "UPDATE nbp.nbp_user SET first_name = ?, last_name = ?, username = ?, email = ?, password = ?, phone_number = ?, "
                    +
                    "role_id = (SELECT id FROM nbp.nbp_role WHERE name = ?) WHERE id = ?";
            jdbcTemplate.update(sql, user.getFirstName(), user.getLastName(), user.getUsername(), user.getEmail(),
                    user.getPasswordHash(), user.getPhone(),
                    user.getRole() != null ? user.getRole() : "REGISTERED_USER", user.getId());
            return user;
        }
    }
}
