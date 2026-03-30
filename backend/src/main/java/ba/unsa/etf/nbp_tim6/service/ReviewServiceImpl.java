package ba.unsa.etf.nbp_tim6.service;

import ba.unsa.etf.nbp_tim6.model.Review;
import ba.unsa.etf.nbp_tim6.repository.abstraction.ReviewRepository;
import ba.unsa.etf.nbp_tim6.repository.abstraction.UserRepository;
import ba.unsa.etf.nbp_tim6.service.abstraction.ReviewService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository repository;
    private final UserRepository userRepository;

    public ReviewServiceImpl(ReviewRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Review> getAll() {
        return repository.findAll();
    }

    @Override
    public Review getById(Integer id) {
        Review review = repository.findById(id);
        if (review == null) {
            throw new RuntimeException("Review not found!");
        }
        return review;
    }

    @Override
    public List<Review> getByAccommodation(Integer accommodationId) {
        return repository.findByAccommodationId(accommodationId);
    }

    @Override
    public List<Review> getByActivity(Integer activityId) {
        return repository.findByActivityId(activityId);
    }

    @Override
    public void create(Review review) {
        if (userRepository.findById(review.getUserId()).isEmpty()) {
            throw new RuntimeException("User with ID " + review.getUserId() + " does not exist!");
        }
        repository.save(review);
    }

    @Override
    public void update(Review review) {
        repository.update(review);
    }

    @Override
    public void delete(Integer id) {
        repository.delete(id);
    }
}
