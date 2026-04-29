import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { forkJoin, map, catchError, of } from 'rxjs';
import { environment } from '../../../environments/environment';

interface TransportationOption {
  id: number;
  provider: string;
  type: string;
  from: string;
  to: string;
  departureLocation: string;
  arrivalLocation: string;
  departureTime: string;
  arrivalTime: string;
  duration: string;
  price: number;
  rating: number;
  available: boolean;
  amenities: string[];
  availableDate: string;
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

  searchTerm: string = '';
  selectedFrom: string = '';
  selectedTo: string = '';
  selectedDate: string = '';
  selectedReturnDate: string = '';
  travelers: number = 1;
  selectedType: string = '';
  maxPrice: number | null = null;
  selectedRating: string = '';

  fromOptions: string[] = [];
  toOptions: string[] = [];
  transportationTypes: string[] = [];

  private readonly transportApiUrl = `${environment.apiUrl}/transport`;
  private readonly transportTypesApiUrl = `${environment.apiUrl}/transport-types`;
  private readonly citiesApiUrl = `${environment.apiUrl}/cities`;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private http: HttpClient
  ) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.selectedFrom = params['from'] || '';
      this.selectedTo = params['to'] || '';
      this.selectedDate = params['departureDate'] || '';
      this.selectedReturnDate = params['returnDate'] || '';
      this.travelers = Number(params['travelers']) || 1;

      this.loadTransportationOptions();
    });
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
              provider: transport.companyName || 'Transport provider',
              type: transportType?.name || 'Transport',
              from: fromCity?.name || 'Unknown city',
              to: toCity?.name || 'Unknown city',
              departureLocation: fromCity?.name || 'Unknown departure location',
              arrivalLocation: toCity?.name || 'Unknown arrival location',
              departureTime: this.formatTime(transport.departureTime),
              arrivalTime: this.formatTime(transport.arrivalTime),
              duration: this.formatDuration(transport.departureTime, transport.arrivalTime),
              price: Number(transport.ticketPrice || 0),
              rating: 4.5,
              available: true,
              amenities: this.buildAmenities(transport),
              availableDate: this.formatDate(transport.departureTime)
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

      const matchesFrom =
        !this.selectedFrom || item.from === this.selectedFrom;

      const matchesTo =
        !this.selectedTo || item.to === this.selectedTo;

      const matchesDate =
        !this.selectedDate || item.availableDate === this.selectedDate;

      const matchesType =
        !this.selectedType || item.type === this.selectedType;

      const matchesPrice =
        this.maxPrice === null || item.price <= this.maxPrice;

      const matchesRating =
        !this.selectedRating || item.rating >= Number(this.selectedRating);

      return (
        matchesSearch &&
        matchesFrom &&
        matchesTo &&
        matchesDate &&
        matchesType &&
        matchesPrice &&
        matchesRating
      );
    });
  }

  resetFilters(): void {
    this.searchTerm = '';
    this.selectedFrom = '';
    this.selectedTo = '';
    this.selectedDate = '';
    this.selectedReturnDate = '';
    this.travelers = 1;
    this.selectedType = '';
    this.maxPrice = null;
    this.selectedRating = '';

    this.filteredTransportationOptions = [...this.transportationOptions];
  }

  bookTransportation(item: TransportationOption): void {
    if (!item.available) return;

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
        checkOut: this.selectedReturnDate,
        guests: this.travelers
      }
    });
  }

  private getUniqueSortedValues(values: string[]): string[] {
    return [...new Set(values.filter(value => !!value && value !== 'Unknown city'))].sort();
  }

  private formatTime(dateTime?: string): string {
    if (!dateTime) {
      return 'Flexible';
    }

    const date = new Date(dateTime);

    if (Number.isNaN(date.getTime())) {
      return 'Flexible';
    }

    return date.toLocaleTimeString('en-GB', {
      hour: '2-digit',
      minute: '2-digit'
    });
  }

  private formatDate(dateTime?: string): string {
    if (!dateTime) {
      return '';
    }

    const date = new Date(dateTime);

    if (Number.isNaN(date.getTime())) {
      return '';
    }

    return date.toISOString().split('T')[0];
  }

  private formatDuration(departureTime?: string, arrivalTime?: string): string {
    if (!departureTime || !arrivalTime) {
      return 'Flexible duration';
    }

    const departure = new Date(departureTime);
    const arrival = new Date(arrivalTime);

    if (Number.isNaN(departure.getTime()) || Number.isNaN(arrival.getTime())) {
      return 'Flexible duration';
    }

    const diffMs = arrival.getTime() - departure.getTime();

    if (diffMs < 0) {
      return 'Flexible duration';
    }

    const hours = Math.floor(diffMs / (1000 * 60 * 60));
    const minutes = Math.floor((diffMs / (1000 * 60)) % 60);

    return `${hours}h ${minutes}m`;
  }

  private buildAmenities(transport: TransportApi): string[] {
    const amenities: string[] = [];

    if (transport.seatNumber) {
      amenities.push(`Seat ${transport.seatNumber}`);
    }

    if (transport.bookingReference) {
      amenities.push(`Booking ref: ${transport.bookingReference}`);
    }

    if (transport.premiumFlag) {
      amenities.push('Premium');
    }

    if (amenities.length === 0) {
      amenities.push('Standard');
    }

    return amenities;
  }
}
