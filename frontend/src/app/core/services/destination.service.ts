import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, forkJoin, map, of, switchMap } from 'rxjs';
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
  description?: string;
}

export interface DestinationHotel {
  id: number;
  destinationId: number;
  name: string;
  imageUrl: string;
  pricePerNight: number;
  rating: number;
  location: string;
  type?: string;
  availableFrom?: string;
  availableTo?: string;
}

export interface DestinationActivity {
  id: number;
  destinationId: number;
  title: string;
  category: string;
  price: number;
  duration: string;
  imageUrl: string;
  rating: number;
  description?: string;
  location?: string;
}

export interface DestinationTransport {
  id: number;
  destinationId: number;
  type: string;
  provider: string;
  price: number;
  duration: string;
  imageUrl: string;
  departureTime?: string;
  arrivalTime?: string;
}

interface Trip {
  id: number;
  title: string;
  description?: string;
  startDate: string;
  endDate: string;
  budget?: number;
  imageUrl?: string;
}

interface TripCity {
  id: number;
  tripId: number;
  cityId: number;
  arrivalDate: string;
  departureDate: string;
}

interface AccommodationApi {
  id: number;
  cityId: number;
  accommodationTypeId: number;
  name: string;
  address?: string;
  description?: string;
  pricePerNight: number;
  stars?: number;
  imageUrl?: string;
}

interface ActivityApi {
  id: number;
  tripId?: number;
  cityId: number;
  activityTypeId: number;
  name: string;
  description?: string;
  location?: string;
  activityDate?: string;
  startTime?: string;
  endTime?: string;
  price?: number;
  imageUrl?: string;
}

interface TransportApi {
  id: number;
  tripId?: number;
  fromCityId: number;
  toCityId: number;
  transportTypeId: number;
  companyName?: string;
  departureTime?: string;
  arrivalTime?: string;
  ticketPrice?: number;
  seatNumber?: string;
  bookingReference?: string;
}

interface TransportTypeApi {
  id: number;
  name: string;
}

@Injectable({
  providedIn: 'root'
})
export class DestinationService {
  private readonly citiesApiUrl = `${environment.apiUrl}/cities`;
  private readonly accommodationsApiUrl = `${environment.apiUrl}/accommodations`;
  private readonly tripsApiUrl = `${environment.apiUrl}/trips`;
  private readonly tripCitiesApiUrl = `${environment.apiUrl}/trip-cities`;
  private readonly activitiesApiUrl = `${environment.apiUrl}/activities`;
  private readonly transportApiUrl = `${environment.apiUrl}/transport`;
  private readonly transportTypesApiUrl = `${environment.apiUrl}/transport-types`;

  constructor(private http: HttpClient) { }

  getAllCities(): Observable<City[]> {
    return forkJoin({
      cities: this.http.get<City[]>(this.citiesApiUrl),
      accommodations: this.http.get<AccommodationApi[]>(this.accommodationsApiUrl)
    }).pipe(
      map(({ cities, accommodations }) =>
        cities.map(city => this.enrichCity(city, accommodations))
      )
    );
  }

  getCityById(id: number): Observable<City | undefined> {
    return forkJoin({
      city: this.http.get<City>(`${this.citiesApiUrl}/${id}`),
      accommodations: this.http.get<AccommodationApi[]>(`${this.accommodationsApiUrl}/city/${id}`)
    }).pipe(
      map(({ city, accommodations }) => this.enrichCity(city, accommodations))
    );
  }

  getTripsByCityId(cityId: number): Observable<DestinationTrip[]> {
    return forkJoin({
      tripCities: this.http.get<TripCity[]>(this.tripCitiesApiUrl),
      trips: this.http.get<Trip[]>(this.tripsApiUrl)
    }).pipe(
      map(({ tripCities, trips }) => {
        const tripIds = new Set(
          tripCities
            .filter(tripCity => tripCity.cityId === cityId)
            .map(tripCity => tripCity.tripId)
        );

        return trips
          .filter(trip => tripIds.has(trip.id))
          .map(trip => ({
            id: trip.id,
            destinationId: cityId,
            title: trip.title,
            imageUrl: trip.imageUrl || 'assets/images/placeholder.jpg',
            duration: this.formatTripDuration(trip.startDate, trip.endDate),
            price: Number(trip.budget || 0),
            description: trip.description || 'No description available.'
          }));
      })
    );
  }

  getHotelsByCityId(cityId: number): Observable<DestinationHotel[]> {
    return this.http.get<AccommodationApi[]>(`${this.accommodationsApiUrl}/city/${cityId}`).pipe(
      map(accommodations =>
        accommodations.map(accommodation => ({
          id: accommodation.id,
          destinationId: cityId,
          name: accommodation.name,
          imageUrl: accommodation.imageUrl || 'assets/images/placeholder.jpg',
          pricePerNight: Number(accommodation.pricePerNight || 0),
          rating: accommodation.stars || 0,
          location: accommodation.address || 'City center',
          type: this.mapAccommodationType(accommodation.accommodationTypeId),
          availableFrom: 'Flexible',
          availableTo: 'Flexible'
        }))
      )
    );
  }

  getActivitiesByCityId(cityId: number): Observable<DestinationActivity[]> {
    return this.http.get<ActivityApi[]>(this.activitiesApiUrl).pipe(
      map(activities =>
        activities
          .filter(activity => activity.cityId === cityId)
          .map(activity => ({
            id: activity.id,
            destinationId: cityId,
            title: activity.name,
            category: this.mapActivityType(activity.activityTypeId),
            price: Number(activity.price || 0),
            duration: this.formatTimeRange(activity.startTime, activity.endTime),
            imageUrl: activity.imageUrl || 'assets/images/placeholder.jpg',
            rating: 4.5,
            description: activity.description || 'No description available.',
            location: activity.location || 'City center'
          }))
      )
    );
  }

  getTransportByCityId(cityId: number): Observable<DestinationTransport[]> {
    return forkJoin({
      transports: this.http.get<TransportApi[]>(this.transportApiUrl),
      transportTypes: this.http.get<TransportTypeApi[]>(this.transportTypesApiUrl)
    }).pipe(
      map(({ transports, transportTypes }) =>
        transports
          .filter(transport => transport.toCityId === cityId || transport.fromCityId === cityId)
          .map(transport => {
            const transportType = transportTypes.find(
              type => type.id === transport.transportTypeId
            );

            return {
              id: transport.id,
              destinationId: cityId,
              type: transportType?.name || 'Transport',
              provider: transport.companyName || 'Transport provider',
              price: Number(transport.ticketPrice || 0),
              duration: this.formatDateTimeRange(transport.departureTime, transport.arrivalTime),
              imageUrl: 'assets/images/placeholder.jpg',
              departureTime: transport.departureTime,
              arrivalTime: transport.arrivalTime
            };
          })
      )
    );
  }

  getAllCountries(): Observable<string[]> {
    return this.getAllCities().pipe(
      map(cities =>
        [...new Set(
          cities
            .map(city => city.countryName)
            .filter((country): country is string => !!country)
        )].sort()
      )
    );
  }

  getDestinationDetails(cityId: number): Observable<{
    city: City | undefined;
    hotels: DestinationHotel[];
    trips: DestinationTrip[];
    activities: DestinationActivity[];
    transportOptions: DestinationTransport[];
  }> {
    return this.getCityById(cityId).pipe(
      switchMap(city => {
        if (!city) {
          return of({
            city: undefined,
            hotels: [],
            trips: [],
            activities: [],
            transportOptions: []
          });
        }

        return forkJoin({
          hotels: this.getHotelsByCityId(cityId),
          trips: this.getTripsByCityId(cityId),
          activities: this.getActivitiesByCityId(cityId),
          transportOptions: this.getTransportByCityId(cityId)
        }).pipe(
          map(({ hotels, trips, activities, transportOptions }) => ({
            city,
            hotels,
            trips,
            activities,
            transportOptions
          }))
        );
      })
    );
  }

  private enrichCity(city: City, accommodations: AccommodationApi[]): City {
    const cityAccommodations = accommodations.filter(accommodation => accommodation.cityId === city.id);

    const averageRating = cityAccommodations.length
      ? cityAccommodations.reduce((sum, accommodation) => sum + (accommodation.stars || 0), 0) / cityAccommodations.length
      : undefined;

    const lowestPrice = cityAccommodations.length
      ? Math.min(...cityAccommodations.map(accommodation => Number(accommodation.pricePerNight || 0)))
      : undefined;

    return {
      ...city,
      imageUrl: city.imageUrl || 'assets/images/placeholder.jpg',
      longDescription: city.description,
      rating: averageRating ? Number(averageRating.toFixed(1)) : undefined,
      reviewCount: cityAccommodations.length || undefined,
      priceFrom: lowestPrice,
      tags: this.buildTags(city)
    };
  }

  private buildTags(city: City): string[] {
    const tags = [city.continent, city.countryName].filter((value): value is string => !!value);

    if (city.description) {
      if (/beach|sea|coast|island/i.test(city.description)) {
        tags.push('Beach');
      }

      if (/history|historic|culture|museum/i.test(city.description)) {
        tags.push('Culture');
      }

      if (/food|cuisine|restaurant/i.test(city.description)) {
        tags.push('Food');
      }
    }

    return [...new Set(tags)];
  }

  private formatTripDuration(startDate?: string, endDate?: string): string {
    if (!startDate || !endDate) {
      return 'Dates TBD';
    }

    const start = new Date(startDate);
    const end = new Date(endDate);
    const diffMs = end.getTime() - start.getTime();
    const days = Math.max(1, Math.round(diffMs / (1000 * 60 * 60 * 24)) + 1);

    return `${days} day${days === 1 ? '' : 's'}`;
  }

  private formatTimeRange(startTime?: string, endTime?: string): string {
    if (!startTime || !endTime) {
      return 'Flexible duration';
    }

    return `${startTime.substring(0, 5)} - ${endTime.substring(0, 5)}`;
  }

  private formatDateTimeRange(departureTime?: string, arrivalTime?: string): string {
    if (!departureTime || !arrivalTime) {
      return 'Flexible duration';
    }

    const departure = new Date(departureTime);
    const arrival = new Date(arrivalTime);

    if (Number.isNaN(departure.getTime()) || Number.isNaN(arrival.getTime())) {
      return 'Flexible duration';
    }

    const diffMs = arrival.getTime() - departure.getTime();

    if (diffMs < 0) {
      return 'Flexible duration';
    }

    const diffHours = Math.floor(diffMs / (1000 * 60 * 60));
    const diffMinutes = Math.floor((diffMs / (1000 * 60)) % 60);

    return `${diffHours}h ${diffMinutes}m`;
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

  private mapActivityType(typeId: number): string {
    const activityTypes: Record<number, string> = {
      1: 'Sightseeing',
      2: 'Museum',
      3: 'Adventure',
      4: 'Food',
      5: 'Culture'
    };

    return activityTypes[typeId] || 'Activity';
  }
}
