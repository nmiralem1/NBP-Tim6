import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AccommodationDetailsItem, AccommodationService } from '../../core/services/accommodation.service';
import { Trip, TripService } from '../../core/services/trip.service';
import { AuthService } from '../../core/services/auth.service';
import { BookingService } from '../../core/services/booking.service';

@Component({
  selector: 'app-accommodation-details',
  templateUrl: './accommodation-details.component.html',
  styleUrls: ['./accommodation-details.component.scss']
})
export class AccommodationDetailsComponent implements OnInit {
  accommodationId: number | null = null;
  accommodation: AccommodationDetailsItem | null = null;

  bookingForm = { checkInDate: '', checkOutDate: '', guests: 2 };
  bookingError = '';

  myTrips: Trip[] = [];
  showTripModal = false;
  tripModalForm = { tripId: null as number | null, checkIn: '', checkOut: '', guests: 1 };
  isAddingToTrip = false;
  addToTripError = '';
  addToTripSuccess = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private accommodationService: AccommodationService,
    private tripService: TripService,
    private bookingService: BookingService,
    public authService: AuthService
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const id = Number(params.get('id'));
      this.accommodationId = id;
      this.loadAccommodationDetails(id);
    });

    this.loadMyTrips();
  }

  private get currentUserId(): number | null {
    const user = localStorage.getItem('currentUser');
    return user ? JSON.parse(user)?.id ?? null : null;
  }

  loadAccommodationDetails(id: number): void {
    this.accommodationService.getAccommodationById(id).subscribe({
      next: accommodation => {
        this.accommodation = accommodation;
      },
      error: () => {
        this.router.navigate(['/accommodations']);
      }
    });
  }

  loadMyTrips(): void {
    const userId = this.currentUserId;

    if (!userId) {
      return;
    }

    this.tripService.getTripsByUserId(userId).subscribe({
      next: trips => this.myTrips = trips,
      error: () => {}
    });
  }

  goBack(): void {
    this.router.navigate(['/accommodations']);
  }

  bookRoom(room: { available: boolean; id: number }): void {
    this.bookingError = '';

    if (!room.available || !this.accommodation) {
      return;
    }

    if (!this.authService.isLoggedIn()) {
      this.router.navigate(['/login']);
      return;
    }

    if (!this.isAccommodationBookingFormValid()) {
      return;
    }

    this.router.navigate(['/book', this.accommodation.id, room.id], {
      queryParams: {
        checkIn: this.bookingForm.checkInDate,
        checkOut: this.bookingForm.checkOutDate,
        guests: this.bookingForm.guests
      }
    });
  }

  private isAccommodationBookingFormValid(): boolean {
    const checkIn = this.bookingForm.checkInDate;
    const checkOut = this.bookingForm.checkOutDate;
    const guests = Number(this.bookingForm.guests);

    if (!checkIn || !checkOut || !guests) {
      this.bookingError = 'Please fill in all the information before continuing to booking.';
      return false;
    }

    if (guests < 1) {
      this.bookingError = 'Number of guests must be at least 1.';
      return false;
    }
    if (this.accommodation?.maxGuests && guests > this.accommodation.maxGuests) {
      this.bookingError = `This accommodation allows up to ${this.accommodation.maxGuests} guests.`;
      return false;
    }

    const today = this.getTodayDateOnly();
    const checkInDate = new Date(checkIn);
    const checkOutDate = new Date(checkOut);

    if (checkInDate < today || checkOutDate < today) {
      this.bookingError = 'Dates cannot be before today.';
      return false;
    }

    if (checkOutDate < checkInDate) {
      this.bookingError = 'Check-out date cannot be before check-in date.';
      return false;
    }

    return true;
  }

  private getTodayDateOnly(): Date {
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    return today;
  }

  openTripModal(): void {
    if (!this.authService.isLoggedIn()) {
      this.router.navigate(['/login']);
      return;
    }

    this.addToTripError = '';
    this.addToTripSuccess = '';
    this.tripModalForm = { tripId: null, checkIn: '', checkOut: '', guests: 1 };
    this.showTripModal = true;
  }

  confirmAddToTrip(): void {
    const userId = this.currentUserId;

    if (!userId || !this.accommodation) {
      return;
    }

    if (!this.tripModalForm.tripId) {
      this.addToTripError = 'Please select a trip.';
      return;
    }

    if (!this.tripModalForm.checkIn || !this.tripModalForm.checkOut || !this.tripModalForm.guests) {
      this.addToTripError = 'Please fill in all the information before continuing to booking.';
      return;
    }

    if (this.tripModalForm.guests < 1) {
      this.addToTripError = 'Number of guests must be at least 1.';
      return;
    }

    const today = this.getTodayDateOnly();
    const checkInDate = new Date(this.tripModalForm.checkIn);
    const checkOutDate = new Date(this.tripModalForm.checkOut);

    if (checkInDate < today || checkOutDate < today) {
      this.addToTripError = 'Dates cannot be before today.';
      return;
    }

    if (checkOutDate < checkInDate) {
      this.addToTripError = 'Check-out date cannot be before check-in date.';
      return;
    }

    if (this.accommodation.maxGuests > 0 && this.tripModalForm.guests > this.accommodation.maxGuests) {
      this.addToTripError = `This accommodation allows up to ${this.accommodation.maxGuests} guests.`;
      return;
    }

    this.isAddingToTrip = true;
    this.addToTripError = '';

    this.bookingService.createBooking({
      userId,
      accommodationId: this.accommodation.id,
      tripId: this.tripModalForm.tripId,
      checkIn: this.tripModalForm.checkIn,
      checkOut: this.tripModalForm.checkOut,
      guestsCount: this.tripModalForm.guests
    }).subscribe({
      next: () => {
        this.isAddingToTrip = false;
        this.showTripModal = false;
        this.addToTripSuccess = 'Hotel added to your trip!';
        setTimeout(() => this.addToTripSuccess = '', 3000);
      },
      error: (err) => {
        this.isAddingToTrip = false;
        this.addToTripError = err?.error?.message || 'Failed to add to trip.';
      }
    });
  }

  searchAvailability(): void {}
}
