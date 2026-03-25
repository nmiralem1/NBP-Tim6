package ba.unsa.etf.nbp_tim6.controller;

import ba.unsa.etf.nbp_tim6.model.Payment;
import ba.unsa.etf.nbp_tim6.service.abstraction.PaymentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService service;

    public PaymentController(PaymentService service) {
        this.service = service;
    }

    @GetMapping
    public List<Payment> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Payment getById(@PathVariable Integer id) {
        return service.getById(id);
    }

    @GetMapping("/trip/{tripId}")
    public List<Payment> getByTrip(@PathVariable Integer tripId) {
        return service.getByTrip(tripId);
    }

    @GetMapping("/user/{userId}")
    public List<Payment> getByUser(@PathVariable Integer userId) {
        return service.getByUser(userId);
    }

    @PostMapping
    public String create(@RequestBody Payment payment) {
        service.create(payment);
        return "Uplata uspješno evidentirana!";
    }

    @PutMapping("/{id}")
    public String update(@PathVariable Integer id, @RequestBody Payment payment) {
        payment.setId(id);
        service.update(payment);
        return "Uplata ažurirana!";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        service.delete(id);
        return "Uplata obrisana!";
    }
}
