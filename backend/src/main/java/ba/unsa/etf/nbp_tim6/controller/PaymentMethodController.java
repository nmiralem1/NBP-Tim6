package ba.unsa.etf.nbp_tim6.controller;

import ba.unsa.etf.nbp_tim6.model.PaymentMethod;
import ba.unsa.etf.nbp_tim6.service.abstraction.PaymentMethodService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment-methods")
public class PaymentMethodController {

    private final PaymentMethodService service;

    public PaymentMethodController(PaymentMethodService service) {
        this.service = service;
    }

    @GetMapping
    public List<PaymentMethod> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public PaymentMethod getById(@PathVariable Integer id) {
        return service.getById(id);
    }

    @PostMapping
    public String create(@RequestBody PaymentMethod method) {
        service.create(method);
        return "Način plaćanja kreiran!";
    }

    @PutMapping("/{id}")
    public String update(@PathVariable Integer id, @RequestBody PaymentMethod method) {
        method.setId(id);
        service.update(method);
        return "Način plaćanja ažuriran!";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        service.delete(id);
        return "Način plaćanja obrisan!";
    }
}
