import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { forkJoin, map, catchError, of } from 'rxjs';
import { environment } from '../../../environments/environment';
import { AuthService } from '../../core/services/auth.service';
import { Trip, TripService } from '../../core/services/trip.service';

interface TransportationOption {
  id: number;
  fromCityId: number;
  toCityId: number;
  transportTypeId: number;
  provider: string;
  type: string;
  from: string;
  to: string;
  departureLocation: string;
  arrivalLocation: string;
  rawDepartureTime: string;
  rawArrivalTime: string;
  departureTime: string;
  arrivalTime: string;
  duration: string;
  price: number;
  rating: number;
  available: boolean;
  amenities: string[];
  availableDate: string;
  seatNumber?: string;
}

interface TransportApi {
  id: number;
  tripId?: number;
  fromCityId: number;
  toCityId: number;
  transportTypeId: number;
  companyName?: string;
  departureTime?: string;
  arrivalTime?: string;
  ticketPrice?: number;
  seatNumber?: string;
  bookingReference?: string;
  premiumFlag?: number;
}

interface TransportTypeApi {
  id: number;
  name: string;
}

interface CityApi {
  id: number;
  name: string;
  countryId?: number;
}

@Component({
  selector: 'app-transportation',
  templateUrl: './transportation.component.html',
  styleUrls: ['./transportation.component.scss']
})
export class TransportationComponent implements OnInit {
  transportationOptions: TransportationOption[] = [];
  filteredTransportationOptions: TransportationOption[] = [];

  searchTerm = '';
  selectedFrom = '';
  selectedTo = '';
  selectedDate = '';
  travelers = 1;
  selectedType = '';
  maxPrice: number | null = null;

  bookingErrorMessage = '';

  fromOptions: string[] = [];
  toOptions: string[] = [];
  transportationTypes: string[] = [];

  minDate = this.getTodayDate();

  myTrips: Trip[] = [];
  showTripModal = false;
  selectedTripId: number | null = null;
  selectedTransport: TransportationOption | null = null;
  isAddingToTrip = false;
  addToTripError = '';
  addToTripSuccess = '';

  private readonly transportApiUrl = `${environment.apiUrl}/transport`;
  private readonly transportTypesApiUrl = `${environment.apiUrl}/transport-types`;
  private readonly citiesApiUrl = `${environment.apiUrl}/cities`;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private http: HttpClient,
    private tripService: TripService,
    public authService: AuthService
  ) {}

  private get currentUserId(): number | null {
    const user = localStorage.getItem('currentUser');
    return user ? JSON.parse(user)?.id ?? null : null;
  }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.selectedFrom = params['from'] || '';
      this.selectedTo = params['to'] || '';
      this.selectedDate = params['departureDate'] || '';
      this.travelers = Number(params['travelers']) || 1;

      this.loadTransportationOptions();
    });

    const userId = this.currentUserId;
    if (userId) {
      this.tripService.getTripsByUserId(userId).subscribe({
        next: trips => this.myTrips = trips,
        error: () => {}
      });
    }
  }

  loadTransportationOptions(): void {
    forkJoin({
      transports: this.http.get<TransportApi[]>(this.transportApiUrl).pipe(
        catchError(error => {
          console.error('Failed to load transport:', error);
          return of([]);
        })
      ),
      transportTypes: this.http.get<TransportTypeApi[]>(this.transportTypesApiUrl).pipe(
        catchError(error => {
          console.error('Failed to load transport types:', error);
          return of([]);
        })
      ),
      cities: this.http.get<CityApi[]>(this.citiesApiUrl).pipe(
        catchError(error => {
          console.error('Failed to load cities:', error);
          return of([]);
        })
      )
    })
      .pipe(
        map(({ transports, transportTypes, cities }) => {
          return transports.map(transport => {
            const fromCity = cities.find(city => city.id === transport.fromCityId);
            const toCity = cities.find(city => city.id === transport.toCityId);
            const transportType = transportTypes.find(type => type.id === transport.transportTypeId);

            return {
              id: transport.id,
              fromCityId: transport.fromCityId,
              toCityId: transport.toCityId,
              transportTypeId: transport.transportTypeId,
              provider: transport.companyName || 'Transport provider',
              type: transportType?.name || 'Transport',
              from: fromCity?.name || 'Unknown city',
              to: toCity?.name || 'Unknown city',
              departureLocation: fromCity?.name || 'Unknown departure location',
              arrivalLocation: toCity?.name || 'Unknown arrival location',
              rawDepartureTime: transport.departureTime || '',
              rawArrivalTime: transport.arrivalTime || '',
              departureTime: this.formatTime(transport.departureTime),
              arrivalTime: this.formatTime(transport.arrivalTime),
              duration: this.formatDuration(transport.departureTime, transport.arrivalTime),
              price: Number(transport.ticketPrice || 0),
              rating: 4.5,
              available: true,
              amenities: this.buildAmenities(transport),
              availableDate: this.formatDate(transport.departureTime),
              seatNumber: transport.seatNumber
            };
          });
        })
      )
      .subscribe({
        next: options => {
          this.transportationOptions = options;
          this.fromOptions = this.getUniqueSortedValues(options.map(item => item.from));
          this.toOptions = this.getUniqueSortedValues(options.map(item => item.to));
          this.transportationTypes = this.getUniqueSortedValues(options.map(item => item.type));

          this.filterTransportation();
        },
        error: error => {
          console.error('Transportation loading failed:', error);
          this.transportationOptions = [];
          this.filteredTransportationOptions = [];
      }
    });
  }

  openTripModal(item: TransportationOption): void {
    if (!this.authService.isLoggedIn()) {
      this.router.navigate(['/login']);
      return;
    }

    this.selectedTransport = item;
    this.selectedTripId = null;
    this.addToTripError = '';
    this.addToTripSuccess = '';
    this.showTripModal = true;
  }

  confirmAddToTrip(): void {
    if (!this.selectedTripId) {
      this.addToTripError = 'Please select a trip.';
      return;
    }

    if (!this.selectedTransport) {
      this.addToTripError = 'Transport option is not available.';
      return;
    }

    this.isAddingToTrip = true;
    this.addToTripError = '';

    this.http.post(this.transportApiUrl, {
      tripId: this.selectedTripId,
      fromCityId: this.selectedTransport.fromCityId,
      toCityId: this.selectedTransport.toCityId,
      transportTypeId: this.selectedTransport.transportTypeId,
      companyName: this.selectedTransport.provider,
      departureTime: this.selectedTransport.rawDepartureTime,
      arrivalTime: this.selectedTransport.rawArrivalTime,
      ticketPrice: this.selectedTransport.price,
      seatNumber: this.selectedTransport.seatNumber || null,
      bookingReference: null
    }, { withCredentials: true, responseType: 'text' }).subscribe({
      next: () => {
        this.isAddingToTrip = false;
        this.showTripModal = false;
        this.addToTripSuccess = 'Transport added to your trip!';
        setTimeout(() => this.addToTripSuccess = '', 3000);
      },
      error: err => {
        this.isAddingToTrip = false;
        this.addToTripError = err?.error || 'Failed to add transport to trip.';
      }
    });
  }

  filterTransportation(): void {
    const term = this.searchTerm.toLowerCase().trim();

    this.filteredTransportationOptions = this.transportationOptions.filter(item => {
      const matchesSearch =
        !term ||
        item.provider.toLowerCase().includes(term) ||
        item.type.toLowerCase().includes(term) ||
        item.from.toLowerCase().includes(term) ||
        item.to.toLowerCase().includes(term) ||
        item.departureLocation.toLowerCase().includes(term) ||
        item.arrivalLocation.toLowerCase().includes(term) ||
        item.amenities.some(amenity => amenity.toLowerCase().includes(term));

      const matchesFrom = !this.selectedFrom || item.from === this.selectedFrom;
      const matchesTo = !this.selectedTo || item.to === this.selectedTo;
      const matchesType = !this.selectedType || item.type === this.selectedType;
      const matchesPrice = this.maxPrice === null || item.price <= this.maxPrice;

      return matchesSearch && matchesFrom && matchesTo && matchesType && matchesPrice;
    });
  }

  resetFilters(): void {
    this.searchTerm = '';
    this.selectedFrom = '';
    this.selectedTo = '';
    this.selectedDate = '';
    this.travelers = 1;
    this.selectedType = '';
    this.maxPrice = null;
    this.bookingErrorMessage = '';

    this.filteredTransportationOptions = [...this.transportationOptions];
  }

  bookTransportation(item: TransportationOption): void {
    this.bookingErrorMessage = '';

    if (!item.available) {
      return;
    }

    if (!this.isBookingFormValid()) {
      return;
    }

    this.router.navigate(['/book/transport', item.id], {
      queryParams: {
        type: 'transport',
        provider: item.provider,
        transportType: item.type,
        from: item.from,
        to: item.to,
        departureLocation: item.departureLocation,
        arrivalLocation: item.arrivalLocation,
        departureTime: item.departureTime,
        arrivalTime: item.arrivalTime,
        duration: item.duration,
        price: item.price,
        checkIn: this.selectedDate,
        checkOut: this.selectedDate,
        guests: this.travelers
      }
    });
  }

  private isBookingFormValid(): boolean {
    if (!this.selectedDate || !this.travelers) {
      this.bookingErrorMessage = 'Please fill in all the information before continuing to booking.';
      return false;
    }

    if (this.selectedDate < this.minDate) {
      this.bookingErrorMessage = 'Date cannot be before today.';
      return false;
    }

    if (Number(this.travelers) < 1) {
      this.bookingErrorMessage = 'Number of travelers must be at least 1.';
      return false;
    }

    return true;
  }

  private getTodayDate(): string {
    const today = new Date();
    const year = today.getFullYear();
    const month = String(today.getMonth() + 1).padStart(2, '0');
    const day = String(today.getDate()).padStart(2, '0');

    return `${year}-${month}-${day}`;
  }

  private getUniqueSortedValues(values: string[]): string[] {
    return [...new Set(values.filter(Boolean))].sort((a, b) => a.localeCompare(b));
  }

  private buildAmenities(transport: TransportApi): string[] {
    const amenities: string[] = [];

    if (transport.seatNumber) {
      amenities.push(`Seat ${transport.seatNumber}`);
    }

    if (transport.bookingReference) {
      amenities.push(`Booking ref: ${transport.bookingReference}`);
    }

    if (transport.premiumFlag === 1) {
      amenities.push('Premium');
    }

    return amenities;
  }

  private formatTime(value?: string): string {
    if (!value) return 'N/A';

    const date = new Date(value);

    if (Number.isNaN(date.getTime())) {
      return value.length >= 5 ? value.substring(0, 5) : value;
    }

    return date.toLocaleTimeString([], {
      hour: '2-digit',
      minute: '2-digit'
    });
  }

  private formatDate(value?: string): string {
    if (!value) return '';

    const date = new Date(value);

    if (Number.isNaN(date.getTime())) {
      return value.substring(0, 10);
    }

    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');

    return `${year}-${month}-${day}`;
  }

  private formatDuration(departure?: string, arrival?: string): string {
    if (!departure || !arrival) return 'N/A';

    const departureDate = new Date(departure);
    const arrivalDate = new Date(arrival);

    if (Number.isNaN(departureDate.getTime()) || Number.isNaN(arrivalDate.getTime())) {
      return 'N/A';
    }

    const diffMs = arrivalDate.getTime() - departureDate.getTime();

    if (diffMs <= 0) return 'N/A';

    const diffMinutes = Math.floor(diffMs / 60000);
    const hours = Math.floor(diffMinutes / 60);
    const minutes = diffMinutes % 60;

    return `${hours}h ${minutes}m`;
  }
}
