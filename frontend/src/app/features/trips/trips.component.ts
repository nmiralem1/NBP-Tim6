import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { EnrichedTrip, Trip, TripService } from '../../core/services/trip.service';
import { AuthService } from '../../core/services/auth.service';
import { DestinationService } from '../../core/services/destination.service';

@Component({
  selector: 'app-trips',
  templateUrl: './trips.component.html',
  styleUrls: ['./trips.component.scss']
})
export class TripsComponent implements OnInit {
  travelPlans: EnrichedTrip[] = [];
  filteredTravelPlans: EnrichedTrip[] = [];
  myTrips: Trip[] = [];

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

  showCreateForm = false;
  isCreating = false;
  createForm = { title: '', startDate: '', endDate: '', budget: 0, description: '' };
  createError = '';

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private tripService: TripService,
    private destinationService: DestinationService,
    public authService: AuthService
  ) {}

  ngOnInit(): void {
    this.loadCityOptions();
    this.loadMyTrips(() => this.loadTravelPlans());

    this.route.queryParams.subscribe(params => {
      const destination = (params['destination'] || params['q'] || '').trim();

      this.searchTerm = destination;
      this.selectedCity = params['city'] || '';
      this.selectedCountry = params['country'] || '';
      this.selectedDate = params['startDate'] || params['date'] || '';
      this.departure = params['departure'] || '';
      this.destination = destination;
      this.returnDate = params['endDate'] || params['returnDate'] || '';
      this.travelers = Number(params['travelers']) || 1;
      this.filterTravelPlans();
    });
  }

  get currentUserId(): number | null {
    const user = localStorage.getItem('currentUser');
    return user ? JSON.parse(user)?.id ?? null : null;
  }

  loadMyTrips(callback?: () => void): void {
    const userId = this.currentUserId;
    if (!userId || !this.authService.isLoggedIn()) {
      callback?.();
      return;
    }
    this.tripService.getTripsByUserId(userId).subscribe({
      next: trips => { this.myTrips = trips; callback?.(); },
      error: () => { callback?.(); }
    });
  }

  loadTravelPlans(): void {
    this.isLoading = true;
    this.errorMessage = '';

    this.tripService.getAllTrips().subscribe({
      next: (trips) => {
        const myIds = new Set(this.myTrips.map(t => t.id));
        const browseable = trips.filter(t => !myIds.has(t.id));
        this.travelPlans = browseable;
        this.filteredTravelPlans = browseable;
        if (this.cities.length === 0) {
          this.cities = [...new Set(trips.map(trip => trip.primaryCityName).filter(Boolean))].sort();
        }
        if (this.countries.length === 0) {
          this.countries = [...new Set(trips.map(trip => trip.primaryCountryName).filter(Boolean))].sort();
        }
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

  loadCityOptions(): void {
    this.destinationService.getAllCities().subscribe({
      next: (cities) => {
        this.cities = [...new Set(cities.map(city => city.name).filter(Boolean))].sort();
        this.countries = [...new Set(
          cities
            .map(city => city.countryName)
            .filter((country): country is string => !!country)
        )].sort();
      },
      error: (err) => console.error('Error loading city options:', err)
    });
  }

  submitCreateTrip(): void {
    const userId = this.currentUserId;
    if (!userId) { this.createError = 'You must be logged in.'; return; }
    if (!this.createForm.title || !this.createForm.startDate || !this.createForm.endDate) {
      this.createError = 'Title, start date and end date are required.';
      return;
    }
    this.isCreating = true;
    this.createError = '';
    this.tripService.createTrip({ ...this.createForm, userId }).subscribe({
      next: () => {
        this.isCreating = false;
        this.showCreateForm = false;
        this.createForm = { title: '', startDate: '', endDate: '', budget: 0, description: '' };
        this.loadMyTrips();
      },
      error: () => {
        this.isCreating = false;
        this.createError = 'Failed to create trip. Please try again.';
      }
    });
  }

  filterTravelPlans(): void {
    const term = this.searchTerm.toLowerCase().trim();

    this.filteredTravelPlans = this.travelPlans.filter(item => {
      const matchesSearch =
        !term ||
        (item.title || '').toLowerCase().includes(term) ||
        (item.shortDescription || '').toLowerCase().includes(term) ||
        (item.primaryCityName || '').toLowerCase().includes(term) ||
        (item.primaryCountryName || '').toLowerCase().includes(term) ||
        (item.locationLabel || '').toLowerCase().includes(term) ||
        item.highlights.some(highlight => highlight.toLowerCase().includes(term));

      const matchesCity = !this.selectedCity || item.primaryCityName === this.selectedCity;
      const matchesCountry = !this.selectedCountry || item.primaryCountryName === this.selectedCountry;
      const matchesDate = this.matchesSelectedDateRange(item);
      const matchesDuration =
        !this.selectedDuration ||
        (this.selectedDuration === '1-3' && item.durationDays >= 1 && item.durationDays <= 3) ||
        (this.selectedDuration === '4-7' && item.durationDays >= 4 && item.durationDays <= 7) ||
        (this.selectedDuration === '8+' && item.durationDays >= 8);
      const matchesBudget = this.maxBudget === null || item.budget <= this.maxBudget;
      const matchesStatus = !this.selectedStatus || item.statusLabel === this.selectedStatus;

      return matchesSearch && matchesCity && matchesCountry && matchesDate && matchesDuration && matchesBudget && matchesStatus;
    });
  }

  private matchesSelectedDateRange(item: EnrichedTrip): boolean {
    if (!this.selectedDate && !this.returnDate) {
      return true;
    }

    if (this.selectedDate && this.returnDate) {
      return item.startDate <= this.returnDate && item.endDate >= this.selectedDate;
    }

    if (this.selectedDate) {
      return item.startDate <= this.selectedDate && item.endDate >= this.selectedDate;
    }

    return item.startDate <= this.returnDate;
  }

  onCityChange(): void {
    if (!this.selectedCity) this.selectedCountry = '';
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
        startDate: this.selectedDate,
        endDate: this.returnDate,
        departure: this.departure,
        destination: this.destination,
        returnDate: this.returnDate,
        travelers: this.travelers
      }
    });
  }
}
