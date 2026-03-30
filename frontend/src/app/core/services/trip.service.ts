import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
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
}

@Injectable({
    providedIn: 'root'
})
export class TripService {
    private apiUrl = `${environment.apiUrl}/trips`;

    constructor(private http: HttpClient) { }

    getAllTrips(): Observable<Trip[]> {
        return this.http.get<Trip[]>(this.apiUrl);
    }

    getTripById(id: number): Observable<Trip> {
        return this.http.get<Trip>(`${this.apiUrl}/${id}`);
    }
}
