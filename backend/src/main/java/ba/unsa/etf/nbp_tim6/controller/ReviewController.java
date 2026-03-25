package ba.unsa.etf.nbp_tim6.controller;

import ba.unsa.etf.nbp_tim6.model.Review;
import ba.unsa.etf.nbp_tim6.service.abstraction.ReviewService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService service;

    public ReviewController(ReviewService service) {
        this.service = service;
    }

    @GetMapping
    public List<Review> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Review getById(@PathVariable Integer id) {
        return service.getById(id);
    }

    @GetMapping("/accommodation/{accommodationId}")
    public List<Review> getByAccommodation(@PathVariable Integer accommodationId) {
        return service.getByAccommodation(accommodationId);
    }

    @GetMapping("/activity/{activityId}")
    public List<Review> getByActivity(@PathVariable Integer activityId) {
        return service.getByActivity(activityId);
    }

    @PostMapping
    public String create(@RequestBody Review review) {
        service.create(review);
        return "Review created!";
    }

    @PutMapping("/{id}")
    public String update(@PathVariable Integer id, @RequestBody Review review) {
        review.setId(id);
        service.update(review);
        return "Review updated!";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        service.delete(id);
        return "Review deleted!";
    }
}
