import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { forkJoin, Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from '../../../environments/environment';

export interface Trip {
    id: number;
    userId: number;
    title: string;
    description?: string;
    startDate: string;
    endDate: string;
    budget: number;
    status: string;
    imageUrl?: string;
    createdAt?: string;
}

export interface TripCity {
    id: number;
    tripId: number;
    cityId: number;
    arrivalDate: string;
    departureDate: string;
    notes?: string;
}

export interface City {
    id: number;
    countryId: number;
    name: string;
    postalCode?: string;
    description?: string;
    imageUrl?: string;
    countryName?: string;
    continent?: string;
}

export interface TripStop {
    id: number;
    cityId: number;
    cityName: string;
    countryName: string;
    imageUrl: string;
    arrivalDate: string;
    departureDate: string;
    notes: string;
}

export interface EnrichedTrip extends Trip {
    durationDays: number;
    primaryCityName: string;
    primaryCountryName: string;
    locationLabel: string;
    highlights: string[];
    stops: TripStop[];
    displayImageUrl: string;
    statusLabel: string;
    shortDescription: string;
}

@Injectable({
    providedIn: 'root'
})
export class TripService {
    private tripsUrl = `${environment.apiUrl}/trips`;
    private tripCitiesUrl = `${environment.apiUrl}/trip-cities`;
    private citiesUrl = `${environment.apiUrl}/cities`;

    constructor(private http: HttpClient) { }

    getAllTrips(): Observable<EnrichedTrip[]> {
        return forkJoin({
            trips: this.http.get<Trip[]>(this.tripsUrl),
            tripCities: this.http.get<TripCity[]>(this.tripCitiesUrl),
            cities: this.http.get<City[]>(this.citiesUrl)
        }).pipe(
            map(({ trips, tripCities, cities }) =>
                trips
                    .map(trip => this.enrichTrip(trip, tripCities, cities))
                    .sort((a, b) => new Date(a.startDate).getTime() - new Date(b.startDate).getTime())
            )
        );
    }

    getTripById(id: number): Observable<EnrichedTrip> {
        return forkJoin({
            trip: this.http.get<Trip>(`${this.tripsUrl}/${id}`),
            tripCities: this.http.get<TripCity[]>(`${this.tripCitiesUrl}/trip/${id}`),
            cities: this.http.get<City[]>(this.citiesUrl)
        }).pipe(
            map(({ trip, tripCities, cities }) => this.enrichTrip(trip, tripCities, cities))
        );
    }

    private enrichTrip(trip: Trip, allTripCities: TripCity[], cities: City[]): EnrichedTrip {
        const cityMap = new Map(cities.map(city => [city.id, city]));
        const stops = allTripCities
            .filter(stop => stop.tripId === trip.id)
            .sort((a, b) => new Date(a.arrivalDate).getTime() - new Date(b.arrivalDate).getTime())
            .map(stop => {
                const city = cityMap.get(stop.cityId);

                return {
                    id: stop.id,
                    cityId: stop.cityId,
                    cityName: city?.name || 'Unknown city',
                    countryName: city?.countryName || 'Unknown country',
                    imageUrl: city?.imageUrl || trip.imageUrl || 'assets/images/placeholder.jpg',
                    arrivalDate: stop.arrivalDate,
                    departureDate: stop.departureDate,
                    notes: stop.notes || ''
                };
            });

        const primaryStop = stops[0];
        const durationDays = this.calculateDurationDays(trip.startDate, trip.endDate);
        const statusLabel = this.formatStatus(trip.status);
        const locationLabel = this.buildLocationLabel(stops);
        const highlights = this.buildHighlights(statusLabel, durationDays, stops);

        return {
            ...trip,
            durationDays,
            primaryCityName: primaryStop?.cityName || 'Multiple destinations',
            primaryCountryName: primaryStop?.countryName || 'Trip',
            locationLabel,
            highlights,
            stops,
            displayImageUrl: trip.imageUrl || primaryStop?.imageUrl || 'assets/images/placeholder.jpg',
            statusLabel,
            shortDescription: trip.description?.trim() || 'This trip does not have a description yet.'
        };
    }

    private calculateDurationDays(startDate: string, endDate: string): number {
        const start = new Date(startDate);
        const end = new Date(endDate);
        const diffMs = end.getTime() - start.getTime();
        const diffDays = Math.round(diffMs / (1000 * 60 * 60 * 24));

        return Math.max(diffDays, 1);
    }

    private formatStatus(status: string | undefined): string {
        if (!status) {
            return 'Planned';
        }

        return status
            .toLowerCase()
            .split('_')
            .map(part => part.charAt(0).toUpperCase() + part.slice(1))
            .join(' ');
    }

    private buildLocationLabel(stops: TripStop[]): string {
        if (stops.length === 0) {
            return 'Trip itinerary';
        }

        if (stops.length === 1) {
            return `${stops[0].cityName}, ${stops[0].countryName}`;
        }

        return `${stops[0].cityName} +${stops.length - 1} more`;
    }

    private buildHighlights(statusLabel: string, durationDays: number, stops: TripStop[]): string[] {
        const highlights = [statusLabel, `${durationDays} days`, `${stops.length || 1} stop${stops.length === 1 ? '' : 's'}`];
        const firstNotes = stops.find(stop => !!stop.notes)?.notes;

        if (firstNotes) {
            highlights.push(firstNotes);
        }

        return highlights.slice(0, 4);
    }
}
