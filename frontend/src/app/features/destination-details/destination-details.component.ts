import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { DestinationHotel, DestinationService, DestinationTrip } from '../../core/services/destination.service';

interface Activity {
    id: number;
    title: string;
    category: string;
    price: number;
    duration: string;
    imageUrl: string;
    rating: number;
}

interface TransportOption {
    id: number;
    type: string;
    provider: string;
    price: number;
    duration: string;
    imageUrl: string;
}

interface DestinationDetails {
    id: number;
    name: string;
    countryName: string;
    continent: string;
    imageUrl: string;
    description: string;
    accommodations: DestinationHotel[];
    activities: Activity[];
    transportOptions: TransportOption[];
    travelPlans: DestinationTrip[];
}

@Component({
    selector: 'app-destination-details',
    templateUrl: './destination-details.component.html',
    styleUrls: ['./destination-details.component.scss']
})
export class DestinationDetailsComponent implements OnInit {
    destinationId: number | null = null;
    destination: DestinationDetails | null = null;
    filteredAccommodations: DestinationHotel[] = [];

    bookingForm = {
        departure: '',
        destination: '',
        departureDate: '',
        returnDate: '',
        travelers: 2
    };

    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private destinationService: DestinationService
    ) {}

    ngOnInit(): void {
        this.route.paramMap.subscribe(params => {
            const id = Number(params.get('id'));
            this.destinationId = id;
            this.loadDestinationDetails(id);
        });
    }

    goToAccommodations(): void {
        this.router.navigate(['/accommodations'], {
            queryParams: { cityId: this.destinationId }
        });
    }

    goToActivities(): void {
        if (!this.destination) return;

        this.router.navigate(['/activities'], {
            queryParams: {
                city: this.destination.name,
                country: this.destination.countryName
            }
        });
    }

    goToTransportation(): void {
        if (!this.destination) return;

        this.router.navigate(['/transportation'], {
            queryParams: {
                from: this.bookingForm.departure,
                to: this.destination.name,
                departureDate: this.bookingForm.departureDate,
                returnDate: this.bookingForm.returnDate,
                travelers: this.bookingForm.travelers
            }
        });
    }

    resetFilters(): void {
        this.bookingForm.departure = '';
        this.bookingForm.destination = this.destination?.name || '';
        this.bookingForm.departureDate = '';
        this.bookingForm.returnDate = '';
        this.bookingForm.travelers = 2;

        if (this.destination) {
            this.filteredAccommodations = [...this.destination.accommodations];
        }
    }

    goToTravelPlans(): void {
        this.router.navigate(['/trips'], {
            queryParams: {
                city: this.destination?.name || '',
                country: this.destination?.countryName || '',
                date: this.bookingForm.departureDate || '',
                returnDate: this.bookingForm.returnDate || '',
                departure: this.bookingForm.departure || '',
                destination: this.bookingForm.destination || this.destination?.name || '',
                travelers: this.bookingForm.travelers || 1
            }
        });
    }

    loadDestinationDetails(id: number): void {
        this.destinationService.getDestinationDetails(id).subscribe({
            next: ({ city, hotels, trips }) => {
                if (!city) {
                    this.router.navigate(['/destinations']);
                    return;
                }

                this.destination = {
                    id: city.id,
                    name: city.name,
                    countryName: city.countryName || 'Unknown country',
                    continent: city.continent || 'Unknown continent',
                    imageUrl: city.imageUrl || 'assets/images/placeholder.jpg',
                    description: city.longDescription || city.description || 'No description available.',
                    accommodations: hotels,
                    activities: [],
                    transportOptions: [],
                    travelPlans: trips
                };

                this.bookingForm.destination = this.destination.name;
                this.filteredAccommodations = [...this.destination.accommodations];
            },
            error: () => {
                this.router.navigate(['/destinations']);
            }
        });
    }

    goBack(): void {
        this.router.navigate(['/destinations']);
    }

    filterAccommodationsByDate(): void {
        if (!this.destination) return;

        const { departureDate, returnDate } = this.bookingForm;

        if (!departureDate || !returnDate) {
            this.filteredAccommodations = [...this.destination.accommodations];
            return;
        }

        this.filteredAccommodations = this.destination.accommodations.filter(item => {
            if (!item.availableFrom || !item.availableTo || item.availableFrom === 'Flexible' || item.availableTo === 'Flexible') {
                return true;
            }

            return departureDate >= item.availableFrom && returnDate <= item.availableTo;
        });
    }

    searchBooking(): void {
        this.filterAccommodationsByDate();
    }
}
