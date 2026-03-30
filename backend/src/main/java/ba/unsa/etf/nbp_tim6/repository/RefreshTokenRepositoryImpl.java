package ba.unsa.etf.nbp_tim6.repository;

import ba.unsa.etf.nbp_tim6.model.RefreshToken;
import ba.unsa.etf.nbp_tim6.repository.abstraction.RefreshTokenRepository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

    private final JdbcTemplate jdbcTemplate;

    public RefreshTokenRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        String sql = "SELECT id, user_id, token, expiry_date FROM refresh_tokens WHERE token = ?";
        List<RefreshToken> tokens = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(RefreshToken.class), token);
        return tokens.stream().findFirst();
    }

    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        if (refreshToken.getId() == null) {
            String sql = "INSERT INTO refresh_tokens (user_id, token, expiry_date) VALUES (?, ?, ?)";
            jdbcTemplate.update(sql, refreshToken.getUserId(), refreshToken.getToken(),
                    Timestamp.from(refreshToken.getExpiryDate()));
            return findByToken(refreshToken.getToken()).orElseThrow();
        } else {
            String sql = "UPDATE refresh_tokens SET user_id = ?, token = ?, expiry_date = ? WHERE id = ?";
            jdbcTemplate.update(sql, refreshToken.getUserId(), refreshToken.getToken(),
                    Timestamp.from(refreshToken.getExpiryDate()), refreshToken.getId());
            return refreshToken;
        }
    }

    @Override
    public void deleteByUserId(Integer userId) {
        String sql = "DELETE FROM refresh_tokens WHERE user_id = ?";
        jdbcTemplate.update(sql, userId);
    }

    @Override
    public void deleteByToken(String token) {
        String sql = "DELETE FROM refresh_tokens WHERE token = ?";
        jdbcTemplate.update(sql, token);
    }
}
