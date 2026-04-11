import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

interface Activity {
  id: number;
  name: string;
  cityName: string;
  countryName: string;
  type: string;
  price: number;
  rating: number;
  imageUrl: string;
  highlights: string[];
  availableFrom: string;
  availableTo: string;
}

@Component({
  selector: 'app-activities',
  templateUrl: './activities.component.html',
  styleUrls: ['./activities.component.scss']
})
export class ActivitiesComponent implements OnInit {
  activities: Activity[] = [];
  filteredActivities: Activity[] = [];

  searchTerm: string = '';
  selectedCity: string = '';
  selectedCountry: string = '';
  selectedDate: string = '';
  selectedType: string = '';
  maxPrice: number | null = null;
  selectedRating: string = '';

  cities: string[] = ['Paris', 'Rome', 'Barcelona'];
  countries: string[] = ['France', 'Italy', 'Spain'];
  activityTypes: string[] = ['Walking Tour', 'Museum', 'Cruise', 'Adventure', 'Food Experience'];

  dummyActivities: Activity[] = [
    {
      id: 1,
      name: 'Paris City Walking Tour',
      cityName: 'Paris',
      countryName: 'France',
      type: 'Walking Tour',
      price: 45,
      rating: 4.8,
      imageUrl: 'assets/images/paris.jpg',
      highlights: ['Local guide', 'Historic landmarks', 'Small group'],
      availableFrom: '2026-04-10',
      availableTo: '2026-05-20'
    },
    {
      id: 2,
      name: 'Louvre Museum Experience',
      cityName: 'Paris',
      countryName: 'France',
      type: 'Museum',
      price: 60,
      rating: 4.9,
      imageUrl: 'assets/images/paris.jpg',
      highlights: ['Skip the line', 'Guide included', 'Top exhibits'],
      availableFrom: '2026-04-12',
      availableTo: '2026-05-30'
    },
    {
      id: 3,
      name: 'Rome Street Food Tour',
      cityName: 'Rome',
      countryName: 'Italy',
      type: 'Food Experience',
      price: 55,
      rating: 4.7,
      imageUrl: 'assets/images/rome.jpg',
      highlights: ['Tastings included', 'Local guide', 'Evening tour'],
      availableFrom: '2026-04-15',
      availableTo: '2026-05-18'
    },
    {
      id: 4,
      name: 'Barcelona Sunset Cruise',
      cityName: 'Barcelona',
      countryName: 'Spain',
      type: 'Cruise',
      price: 70,
      rating: 4.6,
      imageUrl: 'assets/images/barcelona.jpg',
      highlights: ['Sea view', 'Sunset', 'Drinks included'],
      availableFrom: '2026-04-20',
      availableTo: '2026-05-25'
    },
    {
      id: 5,
      name: 'Barcelona Bike Adventure',
      cityName: 'Barcelona',
      countryName: 'Spain',
      type: 'Adventure',
      price: 50,
      rating: 4.5,
      imageUrl: 'assets/images/barcelona.jpg',
      highlights: ['Bike included', 'City highlights', 'Active tour'],
      availableFrom: '2026-04-18',
      availableTo: '2026-05-28'
    }
  ];

  constructor(
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.loadActivities();

    this.route.queryParams.subscribe(params => {
      this.selectedCity = params['city'] || '';
      this.selectedCountry = params['country'] || '';
      this.selectedDate = params['date'] || '';
      this.filterActivities();
    });
  }

  loadActivities(): void {
    this.activities = this.dummyActivities;
    this.filteredActivities = this.dummyActivities;
  }

  filterActivities(): void {
    const term = this.searchTerm.toLowerCase().trim();

    this.filteredActivities = this.activities.filter(item => {
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

      const matchesType =
        !this.selectedType || item.type === this.selectedType;

      const matchesPrice =
        this.maxPrice === null || item.price <= this.maxPrice;

      const matchesRating =
        !this.selectedRating || item.rating >= Number(this.selectedRating);

      return matchesSearch && matchesCity && matchesCountry && matchesDate && matchesType && matchesPrice && matchesRating;
    });
  }

  onCityChange(): void {
    if (!this.selectedCity) {
      this.selectedCountry = '';
    }

    this.filterActivities();
  }

  resetFilters(): void {
    this.searchTerm = '';
    this.selectedCity = '';
    this.selectedCountry = '';
    this.selectedDate = '';
    this.selectedType = '';
    this.maxPrice = null;
    this.selectedRating = '';
    this.filteredActivities = this.activities;
  }

  openActivityDetails(id: number): void {
    this.router.navigate(['/activities', id], {
      queryParams: {
        date: this.selectedDate
      }
    });
  }
}
