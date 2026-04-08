import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

interface TravelPlan {
  id: number;
  name: string;
  cityName: string;
  countryName: string;
  durationDays: number;
  price: number;
  rating: number;
  highlights: string[];
  availableFrom: string;
  availableTo: string;
}

@Component({
  selector: 'app-travel-plans',
  templateUrl: './travelplans.component.html',
  styleUrls: ['./travelplans.component.scss']
})
export class TravelPlansComponent implements OnInit {
  travelPlans: TravelPlan[] = [];
  filteredTravelPlans: TravelPlan[] = [];

  searchTerm: string = '';
  selectedCity: string = '';
  selectedCountry: string = '';
  selectedDate: string = '';
  selectedDuration: string = '';
  maxBudget: number | null = null;
  selectedRating: string = '';

  departure: string = '';
  destination: string = '';
  returnDate: string = '';
  travelers: number = 1;

  cities: string[] = ['Paris', 'Rome', 'Barcelona'];
  countries: string[] = ['France', 'Italy', 'Spain'];

  dummyTravelPlans: TravelPlan[] = [
    {
      id: 1,
      name: 'Romantic Paris Getaway',
      cityName: 'Paris',
      countryName: 'France',
      durationDays: 3,
      price: 420,
      rating: 4.8,
      highlights: ['Hotel included', 'City tour', 'Breakfast'],
      availableFrom: '2026-04-10',
      availableTo: '2026-05-25'
    },
    {
      id: 2,
      name: 'Paris Culture Escape',
      cityName: 'Paris',
      countryName: 'France',
      durationDays: 5,
      price: 650,
      rating: 4.9,
      highlights: ['Museum pass', 'Central hotel', 'Airport transfer'],
      availableFrom: '2026-04-12',
      availableTo: '2026-05-30'
    },
    {
      id: 3,
      name: 'Rome Weekend Adventure',
      cityName: 'Rome',
      countryName: 'Italy',
      durationDays: 3,
      price: 390,
      rating: 4.6,
      highlights: ['Guided tour', 'Breakfast', 'Historic center'],
      availableFrom: '2026-04-15',
      availableTo: '2026-05-20'
    },
    {
      id: 4,
      name: 'Classic Rome Holiday',
      cityName: 'Rome',
      countryName: 'Italy',
      durationDays: 6,
      price: 780,
      rating: 4.7,
      highlights: ['Hotel included', 'Colosseum entry', 'Free day'],
      availableFrom: '2026-04-18',
      availableTo: '2026-05-28'
    },
    {
      id: 5,
      name: 'Barcelona Summer Plan',
      cityName: 'Barcelona',
      countryName: 'Spain',
      durationDays: 4,
      price: 560,
      rating: 4.5,
      highlights: ['Beach stay', 'City highlights', 'Breakfast'],
      availableFrom: '2026-04-20',
      availableTo: '2026-05-26'
    },
    {
      id: 6,
      name: 'Barcelona Premium Escape',
      cityName: 'Barcelona',
      countryName: 'Spain',
      durationDays: 8,
      price: 980,
      rating: 4.8,
      highlights: ['Premium hotel', 'Airport transfer', 'Cruise option'],
      availableFrom: '2026-04-22',
      availableTo: '2026-05-30'
    }
  ];

  constructor(
    private router: Router,
    private route: ActivatedRoute
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
    this.travelPlans = this.dummyTravelPlans;
    this.filteredTravelPlans = this.dummyTravelPlans;
  }

  filterTravelPlans(): void {
    const term = this.searchTerm.toLowerCase().trim();

    this.filteredTravelPlans = this.travelPlans.filter(item => {
      const matchesSearch =
        !term ||
        item.name.toLowerCase().includes(term) ||
        item.cityName.toLowerCase().includes(term) ||
        item.countryName.toLowerCase().includes(term) ||
        item.highlights.some(h => h.toLowerCase().includes(term));

      const matchesCity =
        !this.selectedCity || item.cityName === this.selectedCity;

      const matchesCountry =
        !this.selectedCountry || item.countryName === this.selectedCountry;

      const matchesDate =
        !this.selectedDate ||
        (item.availableFrom <= this.selectedDate && item.availableTo >= this.selectedDate);

      const matchesDuration =
        !this.selectedDuration ||
        (this.selectedDuration === '1-3' && item.durationDays >= 1 && item.durationDays <= 3) ||
        (this.selectedDuration === '4-7' && item.durationDays >= 4 && item.durationDays <= 7) ||
        (this.selectedDuration === '8+' && item.durationDays >= 8);

      const matchesBudget =
        this.maxBudget === null || item.price <= this.maxBudget;

      const matchesRating =
        !this.selectedRating || item.rating >= Number(this.selectedRating);

      return (
        matchesSearch &&
        matchesCity &&
        matchesCountry &&
        matchesDate &&
        matchesDuration &&
        matchesBudget &&
        matchesRating
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
    this.selectedRating = '';
    this.departure = '';
    this.destination = '';
    this.returnDate = '';
    this.travelers = 1;
    this.filteredTravelPlans = this.travelPlans;
  }

  openTravelPlanDetails(id: number): void {
    this.router.navigate(['/travelplans', id], {
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
