package ba.unsa.etf.nbp_tim6.repository.abstraction;

import ba.unsa.etf.nbp_tim6.model.Review;
import java.util.List;

public interface ReviewRepository {
    List<Review> findAll();

    Review findById(Integer id);

    List<Review> findByAccommodationId(Integer accommodationId);

    List<Review> findByActivityId(Integer activityId);

    int save(Review review);

    int update(Review review);

    int delete(Integer id);
}
