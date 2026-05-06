package ba.unsa.etf.nbp_tim6.repository;

import ba.unsa.etf.nbp_tim6.model.ProfileImage;
import ba.unsa.etf.nbp_tim6.model.User;
import ba.unsa.etf.nbp_tim6.repository.abstraction.UserRepository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.io.ByteArrayInputStream;

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
                    user.getRole() != null ? user.getRole() : "USER");
            return findByUsername(user.getUsername()).orElseThrow();
        } else {
            String sql = "UPDATE nbp.nbp_user SET first_name = ?, last_name = ?, username = ?, email = ?, password = ?, phone_number = ?, "
                    +
                    "role_id = (SELECT id FROM nbp.nbp_role WHERE name = ?) WHERE id = ?";
            jdbcTemplate.update(sql, user.getFirstName(), user.getLastName(), user.getUsername(), user.getEmail(),
                    user.getPasswordHash(), user.getPhone(),
                    user.getRole() != null ? user.getRole() : "USER", user.getId());
            return user;
        }
    }

    @Override
    public User updateProfile(Integer id, String firstName, String lastName, String username, String email, String phone) {
        String sql = "UPDATE nbp.nbp_user " +
                "SET first_name = ?, last_name = ?, username = ?, email = ?, phone_number = ? " +
                "WHERE id = ?";

        jdbcTemplate.update(sql, firstName, lastName, username, email, phone, id);

        return findById(id).orElseThrow();
    }

    @Override
    public void saveProfileImage(Integer userId, byte[] imageData, String contentType, String fileName) {
        String updateSql = """
            UPDATE NBPT6.USER_PROFILES
            SET PROFILE_IMAGE = ?,
                PROFILE_IMAGE_CONTENT_TYPE = ?,
                PROFILE_IMAGE_FILE_NAME = ?
            WHERE USER_ID = ?
            """;

        int updatedRows = jdbcTemplate.update(updateSql, ps -> {
            ps.setBinaryStream(1, new ByteArrayInputStream(imageData), imageData.length);
            ps.setString(2, contentType);
            ps.setString(3, fileName);
            ps.setInt(4, userId);
        });

        if (updatedRows == 0) {
            String insertSql = """
                INSERT INTO NBPT6.USER_PROFILES (
                    USER_ID,
                    IMAGE_URL,
                    BIO,
                    PROFILE_IMAGE,
                    PROFILE_IMAGE_CONTENT_TYPE,
                    PROFILE_IMAGE_FILE_NAME
                )
                VALUES (
                    ?,
                    'assets/images/avatar-1.jpg',
                    NULL,
                    ?,
                    ?,
                    ?
                )
                """;

            jdbcTemplate.update(insertSql, ps -> {
                ps.setInt(1, userId);
                ps.setBinaryStream(2, new ByteArrayInputStream(imageData), imageData.length);
                ps.setString(3, contentType);
                ps.setString(4, fileName);
            });
        }
    }

    @Override
    public Optional<ProfileImage> findProfileImageByUserId(Integer userId) {
        String sql = """
                SELECT PROFILE_IMAGE, PROFILE_IMAGE_CONTENT_TYPE, PROFILE_IMAGE_FILE_NAME
                FROM NBPT6.USER_PROFILES
                WHERE USER_ID = ?
                  AND PROFILE_IMAGE IS NOT NULL
                """;

        List<ProfileImage> images = jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new ProfileImage(
                        rs.getBytes("PROFILE_IMAGE"),
                        rs.getString("PROFILE_IMAGE_CONTENT_TYPE"),
                        rs.getString("PROFILE_IMAGE_FILE_NAME")
                ),
                userId
        );

        return images.stream().findFirst();
    }
}