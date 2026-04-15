import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface PaymentMethod {
  id: number;
  name: string;
}

export interface PaymentIntentResponse {
  paymentIntentId: string;
  clientSecret: string;
  status: string;
}

export interface CreatePaymentDto {
  tripId?: number;
  bookingId: number;
  userId: number;
  paymentMethodId: number;
  amount: number;
}

@Injectable({
  providedIn: 'root'
})
export class PaymentService {
  private readonly apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  getPaymentMethods(): Observable<PaymentMethod[]> {
    return this.http.get<PaymentMethod[]>(`${this.apiUrl}/payment-methods`);
  }

  createPaymentIntent(request: {
    amount: number;
    currency: string;
    bookingId: number;
    tripId?: number;
    userId: number;
    paymentMethodId: number;
  }): Observable<PaymentIntentResponse> {
    return this.http.post<PaymentIntentResponse>(
      `${this.apiUrl}/stripe/create-payment-intent`,
      request,
      { withCredentials: true }
    );
  }

  savePaymentRecord(data: CreatePaymentDto): Observable<string> {
    return this.http.post<string>(
      `${this.apiUrl}/payments`,
      {
        bookingId: data.bookingId,
        tripId: data.tripId || null,
        userId: data.userId,
        paymentMethodId: data.paymentMethodId,
        amount: data.amount
      },
      { withCredentials: true, responseType: 'text' as 'json' }
    );
  }
}
