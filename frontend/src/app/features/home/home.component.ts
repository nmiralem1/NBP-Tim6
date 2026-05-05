import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../core/services/auth.service';
import { Router } from '@angular/router';
import { DestinationService } from '../../core/services/destination.service';
import { TripService } from '../../core/services/trip.service';
import { ReviewService } from '../../core/services/review.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  popularDestinations: any[] = [];
  recommendedTrips: any[] = [];
  reviews: any[] = [];
  cityOptions: any[] = [];
  filteredCityOptions: any[] = [];
  showCitySuggestions = false;
  isMenuOpen = false;

  destination = '';
  startDate = '';
  endDate = '';
  travelers = '2';
  searchError = '';

  constructor(
    public authService: AuthService,
    private router: Router,
    private destinationService: DestinationService,
    private tripService: TripService,
    private reviewService: ReviewService
  ) {}

  ngOnInit(): void {
    this.loadDestinations();
    this.loadTrips();
    this.loadReviews();
  }

  loadDestinations(): void {
    this.destinationService.getAllCities().subscribe({
      next: (cities) => {
        this.cityOptions = cities
          .filter(city => !!city.name)
          .map(city => ({
            name: city.name,
            countryName: city.countryName || 'Destination'
          }))
          .sort((a, b) => a.name.localeCompare(b.name));
        this.popularDestinations = cities.slice(0, 4).map(city => ({
          name: city.name,
          image: city.imageUrl || 'assets/images/placeholder.jpg',
          region: city.continent || 'Europe',
          country: city.countryName || 'Destination'
        }));
      },
      error: (err) => console.error('Error loading cities:', err)
    });
  }

  filterCitySuggestions(): void {
    const term = this.destination.trim().toLowerCase();

    if (!term) {
      this.filteredCityOptions = [];
      this.showCitySuggestions = false;
      return;
    }

    this.filteredCityOptions = this.cityOptions
      .filter(city =>
        city.name.toLowerCase().includes(term) ||
        city.countryName.toLowerCase().includes(term)
      )
      .slice(0, 6);
    this.showCitySuggestions = this.filteredCityOptions.length > 0;
  }

  selectCitySuggestion(city: any): void {
    this.destination = city.name;
    this.filteredCityOptions = [];
    this.showCitySuggestions = false;
  }

  hideCitySuggestions(): void {
    setTimeout(() => {
      this.showCitySuggestions = false;
    }, 150);
  }

  loadTrips(): void {
    this.tripService.getAllTrips().subscribe({
      next: (trips) => {
        this.recommendedTrips = trips.slice(0, 3).map(trip => ({
          id: trip.id,
          title: trip.title,
          date: this.formatDate(trip.startDate, trip.endDate),
          price: trip.budget,
          image: trip.imageUrl || 'assets/images/placeholder.jpg'
        }));
      },
      error: (err) => console.error('Error loading trips:', err)
    });
  }

  loadReviews(): void {
    this.reviewService.getAllReviews().subscribe({
      next: (reviews) => {
        this.reviews = reviews.slice(0, 2).map(r => ({
          userName: r.userName || 'Traveler',
          location: r.userLocation || 'World Explorer',
          rating: '⭐'.repeat(r.rating || 5),
          note: r.note
        }));
      },
      error: (err) => console.error('Error loading reviews:', err)
    });
  }

  formatDate(start: string, end: string): string {
    if (!start || !end) return 'TBD';
    const s = new Date(start);
    const e = new Date(end);
    return `${s.getDate()}-${e.getDate()} ${s.toLocaleString('default', { month: 'short' })}, ${s.getFullYear()}`;
  }

  searchTrips(): void {
    const queryParams: any = {};
    const destination = this.destination.trim();

    this.searchError = '';

    if (this.startDate && this.endDate && this.endDate < this.startDate) {
      this.searchError = 'End date must be after start date.';
      return;
    }

    if (destination) {
      queryParams.destination = destination;
      queryParams.q = destination;
    }

    if (this.startDate) {
      queryParams.startDate = this.startDate;
    }

    if (this.endDate) {
      queryParams.endDate = this.endDate;
    }

    if (this.travelers) {
      queryParams.travelers = this.travelers;
    }

    this.router.navigate(['/trips'], { queryParams });
  }

  resetSearch(): void {
    this.destination = '';
    this.startDate = '';
    this.endDate = '';
    this.travelers = '2';
    this.searchError = '';
    this.filteredCityOptions = [];
    this.showCitySuggestions = false;
  }

  toggleMenu(): void {
    this.isMenuOpen = !this.isMenuOpen;
  }

  openTripDetails(tripId: number): void {
    this.router.navigate(['/trips', tripId]);
  }

  logout(): void {
    this.authService.logout().subscribe({
      next: () => {
        this.router.navigate(['/login']);
      },
      error: (err) => {
        console.error('Logout error:', err);
      }
    });
  }
}
