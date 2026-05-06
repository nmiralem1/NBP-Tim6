import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { EnrichedTrip, TripActivity, TripService, TripStop } from '../../core/services/trip.service';
import { BookingDto, BookingService } from '../../core/services/booking.service';
import { Activity, ActivityService } from '../../core/services/activity.service';
import { TransportListItem, TransportService } from '../../core/services/transport.service';
import { AccommodationService } from '../../core/services/accommodation.service';

@Component({
  selector: 'app-trip-details',
  templateUrl: './trip-details.component.html',
  styleUrls: ['./trip-details.component.scss']
})
export class TripDetailsComponent implements OnInit {
  travelPlan: EnrichedTrip | null = null;
  tripBookings: BookingDto[] = [];
  tripActivities: TripActivity[] = [];
  tripTransports: TransportListItem[] = [];
  hotelCityStops: TripStop[] = [];
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
    private activityService: ActivityService,
    private transportService: TransportService,
    private accommodationService: AccommodationService
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
      this.loadTripTransports(id);
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
      next: bookings => {
        this.tripBookings = bookings;
        this.loadHotelCityStops(bookings);
      },
      error: () => {}
    });
  }

  loadHotelCityStops(bookings: BookingDto[]): void {
    this.hotelCityStops = [];

    bookings.forEach(booking => {
      if (!booking.accommodationId) {
        return;
      }

      this.accommodationService.getAccommodationById(booking.accommodationId).subscribe({
        next: accommodation => {
          const alreadyAdded = this.hotelCityStops.some(stop => stop.cityId === accommodation.cityId);
          if (alreadyAdded) {
            return;
          }

          this.hotelCityStops = [
            ...this.hotelCityStops,
            {
              id: -booking.id,
              cityId: accommodation.cityId,
              cityName: accommodation.cityName,
              countryName: accommodation.countryName,
              imageUrl: accommodation.imageUrl,
              arrivalDate: booking.checkIn,
              departureDate: booking.checkOut,
              notes: `Stay at ${accommodation.name}`
            }
          ];
        },
        error: () => {}
      });
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

  loadTripTransports(tripId: number): void {
    this.transportService.getTransportByTripId(tripId).subscribe({
      next: transports => this.tripTransports = transports,
      error: () => {}
    });
  }

  getActivity(activityId: number): Activity | undefined {
    return this.activitiesMap.get(activityId);
  }

  get hotelBookings(): BookingDto[] {
    return this.tripBookings.filter(booking => !!booking.accommodationId);
  }

  get accommodationTotal(): number {
    return this.hotelBookings.reduce((sum, booking) => sum + Number(booking.totalPrice || 0), 0);
  }

  get activitiesTotal(): number {
    return this.tripActivities.reduce((sum, tripActivity) => {
      const activity = this.getActivity(tripActivity.activityId);
      return sum + Number(activity?.price || 0);
    }, 0);
  }

  get transportTotal(): number {
    return this.tripTransports.reduce((sum, transport) => sum + Number(transport.price || 0), 0);
  }

  get tripTotal(): number {
    return this.accommodationTotal + this.activitiesTotal + this.transportTotal;
  }

  get displayedStops(): TripStop[] {
    const stops = [...(this.travelPlan?.stops || [])];

    this.hotelCityStops.forEach(hotelStop => {
      if (!stops.some(stop => stop.cityId === hotelStop.cityId)) {
        stops.push(hotelStop);
      }
    });

    return stops.sort((a, b) => new Date(a.arrivalDate).getTime() - new Date(b.arrivalDate).getTime());
  }

  getTransportBooking(transportId: number): BookingDto | undefined {
    return this.tripBookings.find(booking => booking.transportId === transportId);
  }

  isTransportPaid(transportId: number): boolean {
    return this.getTransportBooking(transportId)?.bookingStatus === 'confirmed';
  }

  getTransportReference(transport: TransportListItem): string {
    return this.getTransportBooking(transport.id)?.bookingReference || `TR-${transport.id}`;
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

  payTransport(transport: TransportListItem): void {
    const existingBooking = this.getTransportBooking(transport.id);

    this.router.navigate(['/book/transport', transport.id], {
      queryParams: {
        type: 'transport',
        tripId: transport.tripId,
        bookingId: existingBooking?.id || null,
        totalPrice: existingBooking?.totalPrice || null,
        bookingReference: existingBooking?.bookingReference || '',
        provider: transport.provider,
        transportType: transport.type,
        from: transport.from,
        to: transport.to,
        fromCityId: transport.fromCityId,
        toCityId: transport.toCityId,
        transportTypeId: transport.transportTypeId,
        departureLocation: transport.from,
        arrivalLocation: transport.to,
        departureTime: transport.departureTime,
        arrivalTime: transport.arrivalTime,
        rawDepartureTime: transport.rawDepartureTime,
        rawArrivalTime: transport.rawArrivalTime,
        duration: transport.duration,
        price: transport.price,
        seatNumber: transport.seatNumber === 'Not assigned' ? '' : transport.seatNumber,
        checkIn: transport.availableDate,
        checkOut: transport.availableDate,
        guests: 1
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
