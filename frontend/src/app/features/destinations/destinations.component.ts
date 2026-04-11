import { Component, OnInit } from '@angular/core';
import { DestinationService, City } from '../../core/services/destination.service';

@Component({
    selector: 'app-destinations',
    templateUrl: './destinations.component.html',
    styleUrls: ['./destinations.component.scss']
})
export class DestinationsComponent implements OnInit {

    countries: string[] = [];
    selectedCountry: string = '';
    allDestinations: City[] = [];
    destinations: City[] = [];
    filteredDestinations: City[] = [];
    searchTerm: string = '';

    constructor(private destinationService: DestinationService) {}

    ngOnInit(): void {
        this.loadDestinations();
        this.loadCountries();
    }

    loadDestinations(): void {
        this.destinationService.getAllCities().subscribe({
            next: (data) => {
                this.destinations = data;
                this.allDestinations = data;
                this.filteredDestinations = data;
            },
            error: (err) => console.error('Error loading destinations:', err)
        });
    }

    loadCountries(): void {
        this.destinationService.getAllCountries().subscribe({
            next: (data) => {
                this.countries = data;
            },
            error: (err) => console.error('Error loading countries:', err)
        });
    }

    filterDestinations(): void {
        const term = this.searchTerm.toLowerCase().trim();

        this.filteredDestinations = this.destinations.filter(dest => {
            const matchesSearch =
                !term ||
                dest.name.toLowerCase().includes(term) ||
                (dest.countryName || '').toLowerCase().includes(term) ||
                (dest.continent || '').toLowerCase().includes(term) ||
                (dest.tags || []).some(tag => tag.toLowerCase().includes(term));

            const matchesCountry =
                !this.selectedCountry || dest.countryName === this.selectedCountry;

            return matchesSearch && matchesCountry;
        });
    }

    resetFilters(): void {
        this.searchTerm = '';
        this.selectedCountry = '';
        this.filteredDestinations = this.destinations;
    }
}
