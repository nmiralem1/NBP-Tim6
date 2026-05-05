import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { EnrichedTrip, TripActivity, TripService } from '../../core/services/trip.service';
import { BookingDto, BookingService } from '../../core/services/booking.service';
import { Activity, ActivityService } from '../../core/services/activity.service';

@Component({
  selector: 'app-trip-details',
  templateUrl: './trip-details.component.html',
  styleUrls: ['./trip-details.component.scss']
})
export class TripDetailsComponent implements OnInit {
  travelPlan: EnrichedTrip | null = null;
  tripBookings: BookingDto[] = [];
  tripActivities: TripActivity[] = [];
  activitiesMap: Map<number, Activity> = new Map();

  bookingInfo = {
    departure: '',
    destination: '',
    departureDate: '',
    returnDate: '',
    travelers: 1
  };

  isLoading = true;
  errorMessage = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private tripService: TripService,
    private bookingService: BookingService,
    private activityService: ActivityService
  ) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.bookingInfo.departure = params['departure'] || '';
      this.bookingInfo.destination = params['destination'] || '';
      this.bookingInfo.departureDate = params['date'] || '';
      this.bookingInfo.returnDate = params['returnDate'] || '';
      this.bookingInfo.travelers = Number(params['travelers']) || 1;
    });

    this.route.paramMap.subscribe(params => {
      const id = Number(params.get('id'));
      if (!id) {
        this.errorMessage = 'Trip could not be found.';
        this.isLoading = false;
        return;
      }
      this.loadTravelPlan(id);
      this.loadTripBookings(id);
      this.loadTripActivities(id);
    });
  }

  loadTravelPlan(id: number): void {
    this.isLoading = true;
    this.errorMessage = '';

    this.tripService.getTripById(id).subscribe({
      next: (trip) => {
        this.travelPlan = trip;
        this.bookingInfo.destination = this.bookingInfo.destination || trip.locationLabel;
        this.bookingInfo.departureDate = trip.startDate;
        this.bookingInfo.returnDate = trip.endDate;
        this.isLoading = false;
      },
      error: () => {
        this.errorMessage = 'Trip details could not be loaded.';
        this.travelPlan = null;
        this.isLoading = false;
      }
    });
  }

  loadTripBookings(tripId: number): void {
    this.bookingService.getBookingsByTripId(tripId).subscribe({
      next: bookings => this.tripBookings = bookings,
      error: () => {}
    });
  }

  loadTripActivities(tripId: number): void {
    this.tripService.getTripActivities(tripId).subscribe({
      next: tripActivities => {
        this.tripActivities = tripActivities;
        tripActivities.forEach(ta => {
          this.activityService.getActivityById(ta.activityId).subscribe({
            next: activity => this.activitiesMap.set(ta.activityId, activity),
            error: () => {}
          });
        });
      },
      error: () => {}
    });
  }

  getActivity(activityId: number): Activity | undefined {
    return this.activitiesMap.get(activityId);
  }

  removeActivity(tripActivityId: number): void {
    this.tripService.removeActivityFromTrip(tripActivityId).subscribe({
      next: () => {
        this.tripActivities = this.tripActivities.filter(ta => ta.id !== tripActivityId);
      },
      error: () => {}
    });
  }

  payBooking(booking: BookingDto): void {
    this.router.navigate(['/book', booking.accommodationId, 0], {
      queryParams: {
        tripId: booking.tripId,
        bookingId: booking.id,
        totalPrice: booking.totalPrice
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/trips'], {
      queryParams: {
        city: this.travelPlan?.primaryCityName || '',
        country: this.travelPlan?.primaryCountryName || '',
        date: this.bookingInfo.departureDate || '',
        departure: this.bookingInfo.departure || '',
        destination: this.bookingInfo.destination || '',
        returnDate: this.bookingInfo.returnDate || '',
        travelers: this.bookingInfo.travelers || 1
      }
    });
  }
}
