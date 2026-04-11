import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

interface AccommodationReview {
    id: number;
    author: string;
    rating: number;
    comment: string;
    date: string;
}

interface RoomOption {
    id: number;
    title: string;
    guests: number;
    pricePerNight: number;
    available: boolean;
}

interface AccommodationDetails {
    id: number;
    name: string;
    cityName: string;
    countryName: string;
    type: string;
    rating: number;
    pricePerNight: number;
    description: string;
    imageUrl: string;
    gallery: string[];
    amenities: string[];
    availableFrom: string;
    availableTo: string;
    address: string;
    checkIn: string;
    checkOut: string;
    roomOptions: RoomOption[];
    reviews: AccommodationReview[];
}

@Component({
    selector: 'app-accommodation-details',
    templateUrl: './accommodation-details.component.html',
    styleUrls: ['./accommodation-details.component.scss']
})
export class AccommodationDetailsComponent implements OnInit {
    accommodationId: number | null = null;
    accommodation: AccommodationDetails | null = null;

    bookingForm = {
        checkInDate: '',
        checkOutDate: '',
        guests: 2
    };

    dummyAccommodations: AccommodationDetails[] = [
        {
            id: 1,
            name: 'Hotel Lumière',
            cityName: 'Paris',
            countryName: 'France',
            type: 'Hotel',
            rating: 4.8,
            pricePerNight: 280,
            description: 'Hotel Lumière offers an elegant stay in the heart of Paris, close to iconic attractions, restaurants, and shopping streets. It is ideal for travelers looking for comfort, style, and a central location.',
            imageUrl: 'assets/images/paris.jpg',
            gallery: [
                'assets/images/paris.jpg',
                'assets/images/paris.jpg',
                'assets/images/paris.jpg'
            ],
            amenities: ['Free Wi-Fi', 'Breakfast included', 'City view', '24/7 reception', 'Airport transfer', 'Air conditioning'],
            availableFrom: '2026-04-10',
            availableTo: '2026-04-30',
            address: '12 Avenue des Voyageurs, Paris, France',
            checkIn: '14:00',
            checkOut: '11:00',
            roomOptions: [
                {
                    id: 1,
                    title: 'Standard Double Room',
                    guests: 2,
                    pricePerNight: 280,
                    available: true
                },
                {
                    id: 2,
                    title: 'Deluxe City View Room',
                    guests: 2,
                    pricePerNight: 340,
                    available: true
                },
                {
                    id: 3,
                    title: 'Family Suite',
                    guests: 4,
                    pricePerNight: 460,
                    available: false
                }
            ],
            reviews: [
                {
                    id: 1,
                    author: 'Emma R.',
                    rating: 4.9,
                    comment: 'Excellent location, very clean rooms, and friendly staff.',
                    date: '2026-03-18'
                },
                {
                    id: 2,
                    author: 'Daniel M.',
                    rating: 4.7,
                    comment: 'Beautiful hotel and great breakfast. Perfect for a city break.',
                    date: '2026-03-25'
                },
                {
                    id: 3,
                    author: 'Sophia L.',
                    rating: 4.8,
                    comment: 'Comfortable stay and amazing atmosphere. Would book again.',
                    date: '2026-04-02'
                }
            ]
        },
        {
            id: 2,
            name: 'Montmartre Suites',
            cityName: 'Paris',
            countryName: 'France',
            type: 'Apartment',
            rating: 4.6,
            pricePerNight: 210,
            description: 'Montmartre Suites is a cozy and stylish apartment stay designed for guests who prefer more privacy and flexibility during their trip.',
            imageUrl: 'assets/images/paris.jpg',
            gallery: [
                'assets/images/paris.jpg',
                'assets/images/paris.jpg',
                'assets/images/paris.jpg'
            ],
            amenities: ['Free Wi-Fi', 'Kitchen', 'Balcony', 'Self check-in'],
            availableFrom: '2026-04-18',
            availableTo: '2026-05-10',
            address: '8 Rue Montmartre, Paris, France',
            checkIn: '15:00',
            checkOut: '10:00',
            roomOptions: [
                {
                    id: 4,
                    title: 'Studio Apartment',
                    guests: 2,
                    pricePerNight: 210,
                    available: true
                },
                {
                    id: 5,
                    title: 'One Bedroom Apartment',
                    guests: 3,
                    pricePerNight: 260,
                    available: true
                }
            ],
            reviews: [
                {
                    id: 4,
                    author: 'Lucas T.',
                    rating: 4.6,
                    comment: 'Great neighborhood and very practical accommodation.',
                    date: '2026-03-12'
                }
            ]
        }
    ];

    constructor(
        private route: ActivatedRoute,
        private router: Router
    ) {}

    ngOnInit(): void {
        this.route.paramMap.subscribe(params => {
            const id = Number(params.get('id'));
            this.accommodationId = id;
            this.loadAccommodationDetails(id);
        });
    }

    loadAccommodationDetails(id: number): void {
        this.accommodation = this.dummyAccommodations.find(item => item.id === id) || null;

        if (!this.accommodation) {
            this.router.navigate(['/accommodations']);
            return;
        }

        /*
        UVEZATI S BAZOM:
        this.accommodationService.getAccommodationById(id).subscribe(...)

        Potrebne backend rute:
        - GET /api/accommodations/{id}
        - GET /api/accommodations/{id}/rooms
        - GET /api/accommodations/{id}/reviews
        - GET /api/accommodations/{id}/gallery

        Tabele:
        - accommodations
        - accommodation_images
        - accommodation_amenities
        - accommodation_rooms
        - accommodation_reviews
        */
    }

    goBack(): void {
        this.router.navigate(['/accommodations']);
    }

bookRoom(room: any): void {
  if (!room.available || !this.accommodation) return;

  this.router.navigate(['/book', this.accommodation.id, room.id]);
}
    searchAvailability(): void {
        console.log('Availability search:', this.bookingForm);

        /*
        UVEZATI S BAZOM:
        filtriranje po:
        - checkInDate
        - checkOutDate
        - guests
        */
    }
}
