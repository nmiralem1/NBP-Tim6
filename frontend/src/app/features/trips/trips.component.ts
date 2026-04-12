import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { EnrichedTrip, TripService } from '../../core/services/trip.service';

@Component({
  selector: 'app-trips',
  templateUrl: './trips.component.html',
  styleUrls: ['./trips.component.scss']
})
export class TripsComponent implements OnInit {
  travelPlans: EnrichedTrip[] = [];
  filteredTravelPlans: EnrichedTrip[] = [];

  searchTerm = '';
  selectedCity = '';
  selectedCountry = '';
  selectedDate = '';
  selectedDuration = '';
  maxBudget: number | null = null;
  selectedStatus = '';

  departure = '';
  destination = '';
  returnDate = '';
  travelers = 1;

  cities: string[] = [];
  countries: string[] = [];
  statuses: string[] = [];

  isLoading = true;
  errorMessage = '';

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private tripService: TripService
  ) {}

  ngOnInit(): void {
    this.loadTravelPlans();

    this.route.queryParams.subscribe(params => {
      this.selectedCity = params['city'] || '';
      this.selectedCountry = params['country'] || '';
      this.selectedDate = params['date'] || '';
      this.departure = params['departure'] || '';
      this.destination = params['destination'] || '';
      this.returnDate = params['returnDate'] || '';
      this.travelers = Number(params['travelers']) || 1;

      this.filterTravelPlans();
    });
  }

  loadTravelPlans(): void {
    this.isLoading = true;
    this.errorMessage = '';

    this.tripService.getAllTrips().subscribe({
      next: (trips) => {
        this.travelPlans = trips;
        this.filteredTravelPlans = trips;
        this.cities = [...new Set(trips.map(trip => trip.primaryCityName).filter(Boolean))].sort();
        this.countries = [...new Set(trips.map(trip => trip.primaryCountryName).filter(Boolean))].sort();
        this.statuses = [...new Set(trips.map(trip => trip.statusLabel).filter(Boolean))].sort();
        this.isLoading = false;
        this.filterTravelPlans();
      },
      error: () => {
        this.errorMessage = 'Trips could not be loaded at the moment.';
        this.travelPlans = [];
        this.filteredTravelPlans = [];
        this.isLoading = false;
      }
    });
  }

  filterTravelPlans(): void {
    const term = this.searchTerm.toLowerCase().trim();

    this.filteredTravelPlans = this.travelPlans.filter(item => {
      const matchesSearch =
        !term ||
        item.title.toLowerCase().includes(term) ||
        item.shortDescription.toLowerCase().includes(term) ||
        item.primaryCityName.toLowerCase().includes(term) ||
        item.primaryCountryName.toLowerCase().includes(term) ||
        item.highlights.some(highlight => highlight.toLowerCase().includes(term));

      const matchesCity =
        !this.selectedCity || item.primaryCityName === this.selectedCity;

      const matchesCountry =
        !this.selectedCountry || item.primaryCountryName === this.selectedCountry;

      const matchesDate =
        !this.selectedDate ||
        (item.startDate <= this.selectedDate && item.endDate >= this.selectedDate);

      const matchesDuration =
        !this.selectedDuration ||
        (this.selectedDuration === '1-3' && item.durationDays >= 1 && item.durationDays <= 3) ||
        (this.selectedDuration === '4-7' && item.durationDays >= 4 && item.durationDays <= 7) ||
        (this.selectedDuration === '8+' && item.durationDays >= 8);

      const matchesBudget =
        this.maxBudget === null || item.budget <= this.maxBudget;

      const matchesStatus =
        !this.selectedStatus || item.statusLabel === this.selectedStatus;

      return (
        matchesSearch &&
        matchesCity &&
        matchesCountry &&
        matchesDate &&
        matchesDuration &&
        matchesBudget &&
        matchesStatus
      );
    });
  }

  onCityChange(): void {
    if (!this.selectedCity) {
      this.selectedCountry = '';
    }

    this.filterTravelPlans();
  }

  resetFilters(): void {
    this.searchTerm = '';
    this.selectedCity = '';
    this.selectedCountry = '';
    this.selectedDate = '';
    this.selectedDuration = '';
    this.maxBudget = null;
    this.selectedStatus = '';
    this.departure = '';
    this.destination = '';
    this.returnDate = '';
    this.travelers = 1;
    this.filteredTravelPlans = this.travelPlans;
  }

  openTravelPlanDetails(id: number): void {
    this.router.navigate(['/trips', id], {
      queryParams: {
        city: this.selectedCity,
        country: this.selectedCountry,
        date: this.selectedDate,
        departure: this.departure,
        destination: this.destination,
        returnDate: this.returnDate,
        travelers: this.travelers
      }
    });
  }
}
