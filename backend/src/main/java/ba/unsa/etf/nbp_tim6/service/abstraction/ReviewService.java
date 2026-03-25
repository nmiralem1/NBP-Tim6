package ba.unsa.etf.nbp_tim6.service.abstraction;

import ba.unsa.etf.nbp_tim6.model.Review;
import java.util.List;

public interface ReviewService {
    List<Review> getAll();

    Review getById(Integer id);

    List<Review> getByAccommodation(Integer accommodationId);

    List<Review> getByActivity(Integer activityId);

    void create(Review review);

    void update(Review review);

    void delete(Integer id);
}
