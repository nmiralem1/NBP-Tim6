import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { EnrichedTrip, TripService } from '../../core/services/trip.service';

@Component({
  selector: 'app-trip-details',
  templateUrl: './trip-details.component.html',
  styleUrls: ['./trip-details.component.scss']
})
export class TripDetailsComponent implements OnInit {
  travelPlan: EnrichedTrip | null = null;

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
    private tripService: TripService
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
