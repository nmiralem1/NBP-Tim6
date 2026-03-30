import { Injectable } from '@angular/core';
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
}
