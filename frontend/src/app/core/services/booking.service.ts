import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface BookingDto {
  id: number;
  tripId: number;
  userId: number;
  accommodationId: number;
  checkIn: string;
  checkOut: string;
  guestsCount: number;
  totalPrice: number;
  bookingStatus: string;
  bookingReference: string;
  createdAt: string;
}

export interface CreateBookingRequest {
  tripId?: number;
  userId: number;
  accommodationId: number;
  checkIn: string;
  checkOut: string;
  guestsCount: number;
}

export interface BookingCreatedResponse {
  bookingId: number;
  totalPrice: number;
  bookingReference: string;
}


@Injectable({
  providedIn: 'root'
})
export class BookingService {
  private readonly apiUrl = 'http://localhost:8080/api/bookings';

  constructor(private http: HttpClient) {}

  getMyBookings(): Observable<BookingDto[]> {
    return this.http.get<BookingDto[]>(`${this.apiUrl}/me`, {
      withCredentials: true
    });
  }

  createBooking(request: CreateBookingRequest): Observable<BookingCreatedResponse> {
    return this.http.post<BookingCreatedResponse>(this.apiUrl, request, {
      withCredentials: true
    });
  }

  getBookingsByTripId(tripId: number): Observable<BookingDto[]> {
    return this.http.get<BookingDto[]>(`${this.apiUrl}/trip/${tripId}`, {
      withCredentials: true
    });
  }
}