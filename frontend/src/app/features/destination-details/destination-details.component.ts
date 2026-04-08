import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

interface Accommodation {
    id: number;
    name: string;
    type: string;
    pricePerNight: number;
    rating: number;
    imageUrl: string;
    availableFrom: string;
    availableTo: string;
}

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

interface TravelPlan {
    id: number;
    title: string;
    duration: string;
    price: number;
    description: string;
    imageUrl: string;
}

interface DestinationDetails {
    id: number;
    name: string;
    countryName: string;
    continent: string;
    imageUrl: string;
    description: string;
    accommodations: Accommodation[];
    activities: Activity[];
    transportOptions: TransportOption[];
    travelPlans: TravelPlan[];
}

@Component({
    selector: 'app-destination-details',
    templateUrl: './destination-details.component.html',
    styleUrls: ['./destination-details.component.scss']
})
export class DestinationDetailsComponent implements OnInit {
    destinationId: number | null = null;
    destination: DestinationDetails | null = null;
    filteredAccommodations: Accommodation[] = [];

    bookingForm = {
        destination: '',
        departureDate: '',
        returnDate: '',
        travelers: 2
    };

    dummyDestinations: DestinationDetails[] = [
        {
            id: 1,
            name: 'Paris',
            countryName: 'France',
            continent: 'Europe',
            imageUrl: 'assets/images/paris.jpg',
            description: 'Paris is the perfect destination for travelers who love art, history, fine dining, and unforgettable city views. Explore iconic landmarks, elegant streets, and a unique cultural atmosphere all year round.',
            accommodations: [
                {
                    id: 1,
                    name: 'Hotel Lumière',
                    type: 'Hotel',
                    pricePerNight: 280,
                    rating: 4.8,
                    imageUrl: 'assets/images/paris.jpg',
                    availableFrom: '2026-04-10',
                    availableTo: '2026-04-30'
                                  },
                {
                    id: 2,
                    name: 'Montmartre Suites',
                    type: 'Apartment',
                    pricePerNight: 210,
                    rating: 4.6,
                    imageUrl: 'assets/images/paris.jpg',
                    availableFrom: '2026-04-18',
                    availableTo: '2026-05-10'
                },
                {
                    id: 3,
                    name: 'Seine View Resort',
                    type: 'Resort',
                    pricePerNight: 340,
                    rating: 4.9,
                    imageUrl: 'assets/images/paris.jpg',
                    availableFrom: '2026-05-01',
                    availableTo: '2026-05-25'
                }
            ],
            activities: [
                {
                    id: 1,
                    title: 'Eiffel Tower Tour',
                    category: 'Sightseeing',
                    price: 60,
                    duration: '2h',
                    imageUrl: 'assets/images/paris.jpg',
                    rating: 4.7
                },
                {
                    id: 2,
                    title: 'Louvre Guided Visit',
                    category: 'Museum',
                    price: 75,
                    duration: '3h',
                    imageUrl: 'assets/images/paris.jpg',
                    rating: 4.9
                },
                {
                    id: 3,
                    title: 'Seine River Cruise',
                    category: 'Cruise',
                    price: 45,
                    duration: '1.5h',
                    imageUrl: 'assets/images/paris.jpg',
                    rating: 4.8
                }
            ],
            transportOptions: [
                {
                    id: 1,
                    type: 'Plane',
                    provider: 'Air France',
                    price: 420,
                    duration: '2h 20min',
                    imageUrl: 'assets/images/paris.jpg'
                },
                {
                    id: 2,
                    type: 'Bus',
                    provider: 'FlixBus',
                    price: 120,
                    duration: '18h',
                    imageUrl: 'assets/images/paris.jpg'
                },
                {
                    id: 3,
                    type: 'Rent a Car',
                    provider: 'Hertz',
                    price: 190,
                    duration: 'Flexible',
                    imageUrl: 'assets/images/paris.jpg'
                },
                {
                    id: 4,
                    type: 'Boat',
                    provider: 'River Connection',
                    price: 95,
                    duration: '4h',
                    imageUrl: 'assets/images/paris.jpg'
                }
            ],
            travelPlans: [
                {
                    id: 1,
                    title: 'Romantic Paris Escape',
                    duration: '3 days',
                    price: 1199,
                    description: 'A curated short getaway including accommodation, a river cruise, and the city’s most iconic attractions.',
                    imageUrl: 'assets/images/paris.jpg'
                },
                {
                    id: 2,
                    title: 'Paris Art & Culture',
                    duration: '5 days',
                    price: 1549,
                    description: 'A richer itinerary focused on museums, galleries, architecture, and authentic city experiences.',
                    imageUrl: 'assets/images/paris.jpg'
                },
                {
                    id: 3,
                    title: 'Paris Budget Explorer',
                    duration: '4 days',
                    price: 899,
                    description: 'A more affordable travel plan with balanced accommodation, activities, and essential sightseeing.',
                    imageUrl: 'assets/images/paris.jpg'
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
            this.destinationId = id;
            this.loadDestinationDetails(id);
        });
    }

    goToAccommodations(): void {
    this.router.navigate(['/accommodations']);

    /*
    UVEZATI S BAZOM:
    this.router.navigate(['/accommodations'], { queryParams: { cityId: this.destinationId } });
    */
}
    loadDestinationDetails(id: number): void {
        this.destination = this.dummyDestinations.find(item => item.id === id) || null;

        if (!this.destination) {
            this.router.navigate(['/destinations']);
            return;
        }

        this.bookingForm.destination = this.destination.name;
        this.filteredAccommodations = [...this.destination.accommodations];

        /*
        UVEZATI S BAZOM:
        - GET /api/cities/{id}
        - GET /api/cities/{id}/accommodations
        - GET /api/cities/{id}/activities
        - GET /api/cities/{id}/transport-options
        - GET /api/cities/{id}/travel-plans
        */
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
            return departureDate >= item.availableFrom && returnDate <= item.availableTo;
        });
    }

    searchBooking(): void {
        this.filterAccommodationsByDate();

        console.log('Booking search:', this.bookingForm);

        /*
        UVEZATI S BAZOM:
        Pretraga ponuda po:
        - destination
        - departureDate
        - returnDate
        - travelers
        */
    }
}
