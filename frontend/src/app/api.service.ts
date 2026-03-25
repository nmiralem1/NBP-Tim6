import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  private baseUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  getCountries(): Observable<any> {
    return this.http.get(`${this.baseUrl}/countries`);
  }

  getTripsByUser(userId: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/trips/user/${userId}`);
  }

  getAccommodations(): Observable<any> {
    return this.http.get(`${this.baseUrl}/accommodations`);
  }
}