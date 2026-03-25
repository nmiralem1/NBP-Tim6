package ba.unsa.etf.nbp_tim6.controller;

import ba.unsa.etf.nbp_tim6.model.Booking;
import ba.unsa.etf.nbp_tim6.service.abstraction.BookingService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public String createBooking(@RequestBody Booking booking) {
        bookingService.createBooking(booking);
        return "Rezervacija kreirana!";
    }

    @GetMapping("/{id}")
    public Booking getBookingById(@PathVariable Integer id) {
        return bookingService.getBookingById(id);
    }

    @GetMapping("/user/{userId}")
    public java.util.List<Booking> getBookingsByUserId(@PathVariable Integer userId) {
        return bookingService.getBookingsByUserId(userId);
    }

    @PutMapping("/{id}")
    public String updateBooking(@PathVariable Integer id, @RequestBody Booking booking) {
        booking.setId(id);
        bookingService.updateBooking(booking);
        return "Rezervacija ažurirana!";
    }

    @DeleteMapping("/{id}")
    public String deleteBooking(@PathVariable Integer id) {
        bookingService.deleteBooking(id);
        return "Rezervacija obrisana!";
    }
}