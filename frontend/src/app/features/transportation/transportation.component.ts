import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

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

  fromOptions: string[] = ['Sarajevo', 'Paris', 'Rome', 'Barcelona'];
  toOptions: string[] = ['Paris', 'Rome', 'Barcelona', 'Milan'];
  transportationTypes: string[] = ['Bus', 'Flight', 'Train'];

  dummyTransportationOptions: TransportationOption[] = [
    {
      id: 1,
      provider: 'FlixBus',
      type: 'Bus',
      from: 'Sarajevo',
      to: 'Paris',
      departureLocation: 'Sarajevo Central Bus Station',
      arrivalLocation: 'Paris Bercy Station',
      departureTime: '07:30',
      arrivalTime: '22:00',
      duration: '14h 30m',
      price: 120,
      rating: 4.4,
      available: true,
      amenities: ['Wi-Fi', 'Air conditioning', 'USB charging'],
      availableDate: '2026-04-20'
    },
    {
      id: 2,
      provider: 'Ryanair',
      type: 'Flight',
      from: 'Sarajevo',
      to: 'Rome',
      departureLocation: 'Sarajevo International Airport',
      arrivalLocation: 'Rome Ciampino Airport',
      departureTime: '10:15',
      arrivalTime: '11:40',
      duration: '1h 25m',
      price: 95,
      rating: 4.2,
      available: true,
      amenities: ['Cabin baggage', 'Online check-in'],
      availableDate: '2026-04-22'
    },
    {
      id: 3,
      provider: 'Trenitalia',
      type: 'Train',
      from: 'Rome',
      to: 'Milan',
      departureLocation: 'Roma Termini',
      arrivalLocation: 'Milano Centrale',
      departureTime: '09:00',
      arrivalTime: '12:10',
      duration: '3h 10m',
      price: 80,
      rating: 4.7,
      available: true,
      amenities: ['Wi-Fi', 'Power outlets', 'Reserved seats'],
      availableDate: '2026-04-25'
    },
    {
      id: 4,
      provider: 'Vueling',
      type: 'Flight',
      from: 'Barcelona',
      to: 'Paris',
      departureLocation: 'Barcelona El Prat Airport',
      arrivalLocation: 'Paris Orly Airport',
      departureTime: '15:20',
      arrivalTime: '17:05',
      duration: '1h 45m',
      price: 110,
      rating: 4.3,
      available: false,
      amenities: ['Cabin baggage', 'Priority boarding'],
      availableDate: '2026-04-28'
    },
    {
      id: 5,
      provider: 'FlixBus',
      type: 'Bus',
      from: 'Rome',
      to: 'Barcelona',
      departureLocation: 'Rome Tiburtina Bus Station',
      arrivalLocation: 'Barcelona Nord Station',
      departureTime: '06:45',
      arrivalTime: '19:30',
      duration: '12h 45m',
      price: 105,
      rating: 4.1,
      available: true,
      amenities: ['Wi-Fi', 'Air conditioning'],
      availableDate: '2026-04-24'
    }
  ];

  constructor(
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.loadTransportationOptions();

    this.route.queryParams.subscribe(params => {
      this.selectedFrom = params['from'] || '';
      this.selectedTo = params['to'] || '';
      this.selectedDate = params['departureDate'] || '';
      this.selectedReturnDate = params['returnDate'] || '';
      this.travelers = Number(params['travelers']) || 1;

      this.filterTransportation();
    });
  }

  loadTransportationOptions(): void {
    this.transportationOptions = this.dummyTransportationOptions;
    this.filteredTransportationOptions = this.dummyTransportationOptions;
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
        item.amenities.some(a => a.toLowerCase().includes(term));

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

      return matchesSearch && matchesFrom && matchesTo && matchesDate && matchesType && matchesPrice && matchesRating;
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
    this.filteredTransportationOptions = this.transportationOptions;
  }

  bookTransportation(item: TransportationOption): void {
    if (!item.available) return;

    this.router.navigate(['/book', item.id, item.id]);
  }
}
