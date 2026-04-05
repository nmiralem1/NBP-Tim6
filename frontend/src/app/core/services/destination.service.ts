/*import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface City {
    id: number;
    name: string;
    countryId: number;
    description?: string;
    imageUrl?: string;
    countryName?: string;
    continent?: string;
}

@Injectable({
    providedIn: 'root'
})
export class DestinationService {
    private apiUrl = `${environment.apiUrl}/cities`;

    constructor(private http: HttpClient) { }

    getAllCities(): Observable<City[]> {
        return this.http.get<City[]>(this.apiUrl);
    }

    getCityById(id: number): Observable<City> {
        return this.http.get<City>(`${this.apiUrl}/${id}`);
    }
}  */


    //DUMMY ZASAD
    import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface City {
    id: number;
    name: string;
    countryId: number;
    description?: string;
    imageUrl?: string;
    countryName?: string;
    continent?: string;
    rating?: number;
    reviewCount?: number;
    priceFrom?: number;
    tags?: string[];
    longDescription?: string;
}

export interface DestinationTrip {
    id: number;
    destinationId: number;
    title: string;
    imageUrl: string;
    duration: string;
    price: number;
}

export interface DestinationHotel {
    id: number;
    destinationId: number;
    name: string;
    imageUrl: string;
    pricePerNight: number;
    rating: number;
    location: string;
}

@Injectable({
    providedIn: 'root'
})
export class DestinationService {
    private apiUrl = `${environment.apiUrl}/cities`;

    constructor(private http: HttpClient) { }

    private dummyCities: City[] = [
        {
            id: 1,
            name: 'Paris',
            countryId: 1,
            countryName: 'France',
            continent: 'Europe',
            description: 'City of lights, romance and iconic landmarks.',
            longDescription: 'Paris is one of the most famous destinations in the world, known for the Eiffel Tower, museums, charming streets and exceptional cuisine.',
            imageUrl: 'assets/images/paris.jpg',
            rating: 4.8,
            reviewCount: 124,
            priceFrom: 220,
            tags: ['Romantic', 'Culture', 'City Break']
        },
        {
            id: 2,
            name: 'Rome',
            countryId: 2,
            countryName: 'Italy',
            continent: 'Europe',
            description: 'Ancient history, architecture and food.',
            longDescription: 'Rome combines historical monuments, vibrant streets and a rich culinary scene, making it perfect for both short and longer stays.',
            imageUrl: 'assets/images/rome.jpg',
            rating: 4.7,
            reviewCount: 98,
            priceFrom: 180,
            tags: ['History', 'Food', 'Architecture']
        },
        {
            id: 3,
            name: 'Istanbul',
            countryId: 3,
            countryName: 'Turkey',
            continent: 'Europe / Asia',
            description: 'A vibrant blend of East and West.',
            longDescription: 'Istanbul offers grand mosques, bazaars, Bosphorus views and a unique cultural fusion across two continents.',
            imageUrl: 'assets/images/istanbul.jpg',
            rating: 4.6,
            reviewCount: 87,
            priceFrom: 160,
            tags: ['Culture', 'Food', 'Shopping']
        },
        {
            id: 4,
            name: 'Santorini',
            countryId: 4,
            countryName: 'Greece',
            continent: 'Europe',
            description: 'White houses, sunsets and sea views.',
            longDescription: 'Santorini is known for its breathtaking sunsets, blue domes, cliffside villages and relaxing island atmosphere.',
            imageUrl: 'assets/images/istanbul.jpg',
            rating: 4.9,
            reviewCount: 141,
            priceFrom: 320,
            tags: ['Beach', 'Luxury', 'Romantic']
        }
    ];

    private dummyTrips: DestinationTrip[] = [
        {
            id: 1,
            destinationId: 1,
            title: 'Weekend in Paris',
            imageUrl: 'assets/images/trip-paris.jpg',
            duration: '3 days',
            price: 450
        },
        {
            id: 2,
            destinationId: 1,
            title: 'Paris Art & Culture Tour',
            imageUrl: 'assets/images/trip-paris-2.jpg',
            duration: '5 days',
            price: 690
        },
        {
            id: 3,
            destinationId: 2,
            title: 'Roman Adventure',
            imageUrl: 'assets/images/trip-rome.jpg',
            duration: '4 days',
            price: 520
        },
        {
            id: 4,
            destinationId: 4,
            title: 'Santorini Escape',
            imageUrl: 'assets/images/trip-santorini.jpg',
            duration: '5 days',
            price: 890
        }
    ];

    private dummyHotels: DestinationHotel[] = [
        {
            id: 1,
            destinationId: 1,
            name: 'Grand Paris Hotel',
            imageUrl: 'assets/images/hotel-paris.jpg',
            pricePerNight: 160,
            rating: 4.7,
            location: 'Central Paris'
        },
        {
            id: 2,
            destinationId: 1,
            name: 'River View Suites',
            imageUrl: 'assets/images/hotel-paris-2.jpg',
            pricePerNight: 210,
            rating: 4.8,
            location: 'Near Seine'
        },
        {
            id: 3,
            destinationId: 2,
            name: 'Roma Palace',
            imageUrl: 'assets/images/hotel-rome.jpg',
            pricePerNight: 145,
            rating: 4.5,
            location: 'Historic Center'
        },
        {
            id: 4,
            destinationId: 4,
            name: 'Sunset Caldera Resort',
            imageUrl: 'assets/images/hotel-santorini.jpg',
            pricePerNight: 280,
            rating: 4.9,
            location: 'Oia'
        }
    ];

    getAllCities(): Observable<City[]> {
        // PRIVREMENO: dummy podaci za statički frontend
        return of(this.dummyCities);

        // UVEZATI S BAZOM
        // Tabele: cities, countries
        // Ruta: GET /api/cities
        // return this.http.get<City[]>(this.apiUrl);
    }

    getCityById(id: number): Observable<City | undefined> {
        // PRIVREMENO: dummy podaci za statički frontend
        return of(this.dummyCities.find(city => city.id === id));

        // UVEZATI S BAZOM
        // Tabele: cities, countries
        // Ruta: GET /api/cities/:id
        // return this.http.get<City>(`${this.apiUrl}/${id}`);
    }

    getTripsByCityId(cityId: number): Observable<DestinationTrip[]> {
        // PRIVREMENO: dummy podaci za statički frontend
        return of(this.dummyTrips.filter(trip => trip.destinationId === cityId));

        // UVEZATI S BAZOM
        // Tabele: trips, trip_dates, cities
        // Ruta: GET /api/cities/:id/trips
        // return this.http.get<DestinationTrip[]>(`${this.apiUrl}/${cityId}/trips`);
    }

    getHotelsByCityId(cityId: number): Observable<DestinationHotel[]> {
        // PRIVREMENO: dummy podaci za statički frontend
        return of(this.dummyHotels.filter(hotel => hotel.destinationId === cityId));

        // UVEZATI S BAZOM
        // Tabele: accommodations, accommodation_types, cities
        // Ruta: GET /api/cities/:id/hotels
        // return this.http.get<DestinationHotel[]>(`${this.apiUrl}/${cityId}/hotels`);
    }

     getAllCountries(): Observable<string[]> {
        const countries = [...new Set(
            this.dummyCities
                .map(city => city.countryName)
                .filter((country): country is string => !!country)
        )].sort();

        return of(countries);
    }

}

