import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AccommodationListItem, AccommodationService } from '../../core/services/accommodation.service';

@Component({
    selector: 'app-accommodations',
    templateUrl: './accommodations.component.html',
    styleUrls: ['./accommodations.component.scss']
})
export class AccommodationsComponent implements OnInit {
    accommodations: AccommodationListItem[] = [];
    filteredAccommodations: AccommodationListItem[] = [];

    searchTerm: string = '';
    selectedCity: string = '';
    selectedType: string = '';
    maxPrice: number | null = null;
    selectedRating: string = '';

    cities: string[] = [];
    accommodationTypes: string[] = [];

    constructor(
        private router: Router,
        private route: ActivatedRoute,
        private accommodationService: AccommodationService
    ) {}

    ngOnInit(): void {
        this.loadAccommodations();
    }

    loadAccommodations(): void {
        this.accommodationService.getAllAccommodations().subscribe({
            next: data => {
                this.accommodations = data;
                this.cities = [...new Set(data.map(item => item.cityName).filter(Boolean))].sort();
                this.accommodationTypes = [...new Set(data.map(item => item.type).filter(Boolean))].sort();

                const cityId = Number(this.route.snapshot.queryParamMap.get('cityId'));
                if (cityId) {
                    const selectedAccommodation = data.find(item => item.cityId === cityId);
                    this.selectedCity = selectedAccommodation?.cityName || '';
                }

                this.filterAccommodations();
            },
            error: err => console.error('Error loading accommodations:', err)
        });
    }

    filterAccommodations(): void {
        const term = this.searchTerm.toLowerCase().trim();

        this.filteredAccommodations = this.accommodations.filter(item => {
            const matchesSearch =
                !term ||
                item.name.toLowerCase().includes(term) ||
                item.cityName.toLowerCase().includes(term) ||
                item.countryName.toLowerCase().includes(term) ||
                item.amenities.some(amenity => amenity.toLowerCase().includes(term));

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
    }
}
