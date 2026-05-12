import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface InvoiceMeta {
  id: number;
  paymentId: number;
  bookingId: number;
  userId: number;
  invoiceNumber: string;
  issuedAt: string;
  totalAmount: number;
}

@Injectable({
  providedIn: 'root'
})
export class InvoiceService {
  private readonly apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  getByBookingId(bookingId: number): Observable<InvoiceMeta> {
    return this.http.get<InvoiceMeta>(
      `${this.apiUrl}/invoices/booking/${bookingId}`,
      { withCredentials: true }
    );
  }

  downloadPdfByBookingId(bookingId: number): Observable<Blob> {
    return this.http.get(
      `${this.apiUrl}/invoices/booking/${bookingId}/pdf`,
      { withCredentials: true, responseType: 'blob' }
    );
  }
}
