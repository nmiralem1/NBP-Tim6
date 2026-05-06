import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface TransportListItem {
  id: number;
  tripId?: number;
  fromCityId: number;
  toCityId: number;
  transportTypeId: number;
  provider?: string;
  type?: string;
  from?: string;
  to?: string;
  departureLocation?: string;
  arrivalLocation?: string;
  departureTime?: string;
  arrivalTime?: string;
  rawDepartureTime?: string;
  rawArrivalTime?: string;
  duration?: string;
  price?: number;
  availableDate?: string;
  seatNumber?: string;
  bookingReference?: string;
  companyName?: string;
  ticketPrice?: number;
  premiumFlag?: number;
}

@Injectable({
  providedIn: 'root'
})
export class TransportService {

  constructor(private http: HttpClient) { }

  getTransportByTripId(tripId: number): Observable<TransportListItem[]> {
    return this.http.get<TransportListItem[]>(`${environment.apiUrl}/transport/trip/${tripId}`);
  }

  getTransportById(id: number): Observable<TransportListItem> {
    return this.http.get<TransportListItem>(`${environment.apiUrl}/transport/${id}`);
  }
}
