import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { forkJoin, map, Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { City, DestinationService } from './destination.service';
import { Review } from './review.service';

interface AccommodationApi {
    id: number;
    cityId: number;
    accommodationTypeId: number;
    name: string;
    address?: string;
    description?: string;
    pricePerNight: number;
    maxGuests: number;
    stars?: number;
    phone?: string;
    email?: string;
    imageUrl?: string;
}

export interface AccommodationListItem {
    id: number;
    name: string;
    cityId: number;
    cityName: string;
    countryName: string;
    type: string;
    pricePerNight: number;
    maxGuests: number;
    rating: number;
    imageUrl: string;
    amenities: string[];
    address: string;
    description: string;
}

export interface AccommodationDetailsItem extends AccommodationListItem {
    gallery: string[];
    checkIn?: string;
    checkOut?: string;
    roomOptions: {
        id: number;
        title: string;
        guests: number;
        pricePerNight: number;
        available: boolean;
    }[];
    reviews: {
        id: number;
        author: string;
        rating: number;
        comment: string;
        date: string;
    }[];
}

@Injectable({
    providedIn: 'root'
})
export class AccommodationService {
    private readonly apiUrl = `${environment.apiUrl}/accommodations`;
    private readonly reviewsApiUrl = `${environment.apiUrl}/reviews`;

    constructor(
        private http: HttpClient,
        private destinationService: DestinationService
    ) {}

    getAllAccommodations(): Observable<AccommodationListItem[]> {
        return forkJoin({
            accommodations: this.http.get<AccommodationApi[]>(this.apiUrl),
            cities: this.destinationService.getAllCities()
        }).pipe(
            map(({ accommodations, cities }) =>
                accommodations.map(accommodation => this.mapAccommodation(accommodation, cities))
            )
        );
    }

    getAccommodationById(id: number): Observable<AccommodationDetailsItem> {
        return forkJoin({
            accommodation: this.http.get<AccommodationApi>(`${this.apiUrl}/${id}`),
            cities: this.destinationService.getAllCities(),
            reviews: this.http.get<Review[]>(`${this.reviewsApiUrl}/accommodation/${id}`)
        }).pipe(
            map(({ accommodation, cities, reviews }) => {
                const mapped = this.mapAccommodation(accommodation, cities);

                return {
                    ...mapped,
                    gallery: mapped.imageUrl ? [mapped.imageUrl] : [],
                    checkIn: undefined,
                    checkOut: undefined,
                    roomOptions: [],
                    reviews: reviews.map(review => ({
                        id: review.id,
                        author: review.userName || 'Traveler',
                        rating: review.rating,
                        comment: review.note || '',
                        date: this.formatReviewDate(review.createdAt)
                    }))
                };
            })
        );
    }

    private mapAccommodation(accommodation: AccommodationApi, cities: City[]): AccommodationListItem {
        const city = cities.find(item => item.id === accommodation.cityId);

        return {
            id: accommodation.id,
            name: accommodation.name,
            cityId: accommodation.cityId,
            cityName: city?.name || 'Unknown city',
            countryName: city?.countryName || 'Unknown country',
            type: this.mapAccommodationType(accommodation.accommodationTypeId),
            pricePerNight: Number(accommodation.pricePerNight || 0),
            maxGuests: accommodation.maxGuests || 0,
            rating: Number(accommodation.stars || 0),
            imageUrl: accommodation.imageUrl || 'assets/images/placeholder.jpg',
            amenities: this.buildAmenities(accommodation),
            address: accommodation.address || 'Address not available',
            description: accommodation.description || 'No description available.'
        };
    }

    private buildAmenities(accommodation: AccommodationApi): string[] {
        const amenities: string[] = [];

        if (accommodation.maxGuests) {
            amenities.push(`Up to ${accommodation.maxGuests} guests`);
        }
        if (accommodation.phone) {
            amenities.push('Phone support');
        }
        if (accommodation.email) {
            amenities.push('Email contact');
        }

        return amenities;
    }

    private mapAccommodationType(typeId: number): string {
        const accommodationTypes: Record<number, string> = {
            1: 'Hotel',
            2: 'Apartment',
            3: 'Hostel',
            4: 'Resort',
            5: 'Villa'
        };

        return accommodationTypes[typeId] || 'Accommodation';
    }

    private formatReviewDate(date: string): string {
        if (!date) {
            return '';
        }

        return new Date(date).toLocaleDateString('en-GB');
    }
}
