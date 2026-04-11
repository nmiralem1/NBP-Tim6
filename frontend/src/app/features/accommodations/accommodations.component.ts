import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

interface Accommodation {
    id: number;
    name: string;
    cityName: string;
    countryName: string;
    type: string;
    pricePerNight: number;
    rating: number;
    imageUrl: string;
    amenities: string[];
    availableFrom: string;
    availableTo: string;
}

@Component({
    selector: 'app-accommodations',
    templateUrl: './accommodations.component.html',
    styleUrls: ['./accommodations.component.scss']
})
export class AccommodationsComponent implements OnInit {
    accommodations: Accommodation[] = [];
    filteredAccommodations: Accommodation[] = [];

    searchTerm: string = '';
    selectedCity: string = '';
    selectedType: string = '';
    maxPrice: number | null = null;
    selectedRating: string = '';

    cities: string[] = ['Paris', 'Rome', 'Barcelona'];
    accommodationTypes: string[] = ['Hotel', 'Apartment', 'Resort', 'Hostel'];

    dummyAccommodations: Accommodation[] = [
        {
            id: 1,
            name: 'Hotel Lumière',
            cityName: 'Paris',
            countryName: 'France',
            type: 'Hotel',
            pricePerNight: 280,
            rating: 4.8,
            imageUrl: 'assets/images/paris.jpg',
            amenities: ['Wi-Fi', 'Breakfast', 'City View'],
            availableFrom: '2026-04-10',
            availableTo: '2026-04-30'
        },
        {
            id: 2,
            name: 'Montmartre Suites',
            cityName: 'Paris',
            countryName: 'France',
            type: 'Apartment',
            pricePerNight: 210,
            rating: 4.6,
            imageUrl: 'assets/images/paris.jpg',
            amenities: ['Wi-Fi', 'Kitchen', 'Balcony'],
            availableFrom: '2026-04-18',
            availableTo: '2026-05-10'
        },
        {
            id: 3,
            name: 'Seine View Resort',
            cityName: 'Paris',
            countryName: 'France',
            type: 'Resort',
            pricePerNight: 340,
            rating: 4.9,
            imageUrl: 'assets/images/paris.jpg',
            amenities: ['Pool', 'Spa', 'Breakfast'],
            availableFrom: '2026-05-01',
            availableTo: '2026-05-25'
        },
        {
            id: 4,
            name: 'Roma Central Stay',
            cityName: 'Rome',
            countryName: 'Italy',
            type: 'Hotel',
            pricePerNight: 240,
            rating: 4.5,
            imageUrl: 'assets/images/rome.jpg',
            amenities: ['Wi-Fi', 'Breakfast', 'Airport Transfer'],
            availableFrom: '2026-04-12',
            availableTo: '2026-05-18'
        },
        {
            id: 5,
            name: 'Barcelona Beach Apartment',
            cityName: 'Barcelona',
            countryName: 'Spain',
            type: 'Apartment',
            pricePerNight: 190,
            rating: 4.7,
            imageUrl: 'assets/images/barcelona.jpg',
            amenities: ['Wi-Fi', 'Kitchen', 'Near Beach'],
            availableFrom: '2026-04-15',
            availableTo: '2026-05-22'
        }
    ];

    constructor(private router: Router) {}

    ngOnInit(): void {
        this.loadAccommodations();
    }

    loadAccommodations(): void {
        this.accommodations = this.dummyAccommodations;
        this.filteredAccommodations = this.dummyAccommodations;

        /*
        UVEZATI S BAZOM:
        Ovdje kasnije ide servis, npr:
        this.accommodationService.getAllAccommodations().subscribe(...)

        Potrebne backend rute:
        - GET /api/accommodations
        - GET /api/accommodations/{id}

        Tabele:
        - accommodations
        - cities
        - countries
        - accommodation_amenities
        - accommodation_images
        */
    }

    filterAccommodations(): void {
        const term = this.searchTerm.toLowerCase().trim();

        this.filteredAccommodations = this.accommodations.filter(item => {
            const matchesSearch =
                !term ||
                item.name.toLowerCase().includes(term) ||
                item.cityName.toLowerCase().includes(term) ||
                item.countryName.toLowerCase().includes(term) ||
                item.amenities.some(a => a.toLowerCase().includes(term));

            const matchesCity =
                !this.selectedCity || item.cityName === this.selectedCity;

            const matchesType =
                !this.selectedType || item.type === this.selectedType;

            const matchesPrice =
                this.maxPrice === null || item.pricePerNight <= this.maxPrice;

            const matchesRating =
                !this.selectedRating || item.rating >= Number(this.selectedRating);

            return matchesSearch && matchesCity && matchesType && matchesPrice && matchesRating;
        });
    }

    resetFilters(): void {
        this.searchTerm = '';
        this.selectedCity = '';
        this.selectedType = '';
        this.maxPrice = null;
        this.selectedRating = '';
        this.filteredAccommodations = this.accommodations;
    }

    openAccommodationDetails(id: number): void {
        this.router.navigate(['/accommodations', id]);

        /*
        UVEZATI S BAZOM:
        Ovo vodi na accommodation details screen.
        Ruta koju kasnije dodaješ:
        /accommodations/:id
        */
    }
}
