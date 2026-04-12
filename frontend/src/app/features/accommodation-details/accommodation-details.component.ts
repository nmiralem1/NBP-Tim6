import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AccommodationDetailsItem, AccommodationService } from '../../core/services/accommodation.service';

@Component({
    selector: 'app-accommodation-details',
    templateUrl: './accommodation-details.component.html',
    styleUrls: ['./accommodation-details.component.scss']
})
export class AccommodationDetailsComponent implements OnInit {
    accommodationId: number | null = null;
    accommodation: AccommodationDetailsItem | null = null;

    bookingForm = {
        checkInDate: '',
        checkOutDate: '',
        guests: 2
    };

    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private accommodationService: AccommodationService
    ) {}

    ngOnInit(): void {
        this.route.paramMap.subscribe(params => {
            const id = Number(params.get('id'));
            this.accommodationId = id;
            this.loadAccommodationDetails(id);
        });
    }

    loadAccommodationDetails(id: number): void {
        this.accommodationService.getAccommodationById(id).subscribe({
            next: accommodation => {
                this.accommodation = accommodation;
            },
            error: () => {
                this.router.navigate(['/accommodations']);
            }
        });
    }

    goBack(): void {
        this.router.navigate(['/accommodations']);
    }

    bookRoom(room: { available: boolean; id: number }): void {
        if (!room.available || !this.accommodation) return;

        this.router.navigate(['/book', this.accommodation.id, room.id]);
    }

    searchAvailability(): void {
        console.log('Availability search:', this.bookingForm);
    }
}
