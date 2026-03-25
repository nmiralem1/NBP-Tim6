package ba.unsa.etf.nbp_tim6.repository;

import ba.unsa.etf.nbp_tim6.model.Review;
import ba.unsa.etf.nbp_tim6.repository.abstraction.ReviewRepository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReviewRepositoryImpl implements ReviewRepository {

    private final JdbcTemplate jdbcTemplate;

    public ReviewRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Review> findAll() {
        String sql = "SELECT id, user_id, accommodation_id, activity_id, rating, note, created_at FROM reviews";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Review.class));
    }

    @Override
    public Review findById(Integer id) {
        String sql = "SELECT id, user_id, accommodation_id, activity_id, rating, note, created_at FROM reviews WHERE id = ?";
        List<Review> results = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Review.class), id);
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public List<Review> findByAccommodationId(Integer accommodationId) {
        String sql = "SELECT id, user_id, accommodation_id, activity_id, rating, note, created_at FROM reviews WHERE accommodation_id = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Review.class), accommodationId);
    }

    @Override
    public List<Review> findByActivityId(Integer activityId) {
        String sql = "SELECT id, user_id, accommodation_id, activity_id, rating, note, created_at FROM reviews WHERE activity_id = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Review.class), activityId);
    }

    @Override
    public int save(Review review) {
        String sql = "INSERT INTO reviews (user_id, accommodation_id, activity_id, rating, note) VALUES (?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, review.getUserId(), review.getAccommodationId(), review.getActivityId(),
                review.getRating(), review.getNote());
    }

    @Override
    public int update(Review review) {
        String sql = "UPDATE reviews SET user_id = ?, accommodation_id = ?, activity_id = ?, rating = ?, note = ? WHERE id = ?";
        return jdbcTemplate.update(sql, review.getUserId(), review.getAccommodationId(), review.getActivityId(),
                review.getRating(), review.getNote(), review.getId());
    }

    @Override
    public int delete(Integer id) {
        String sql = "DELETE FROM reviews WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
