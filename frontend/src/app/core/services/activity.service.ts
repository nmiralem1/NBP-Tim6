import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface Activity {
  id: number;
  tripId: number;
  cityId: number;
  activityTypeId: number;
  name: string;
  description: string;
  location: string;
  activityDate: string;
  startTime: string;
  endTime: string;
  price: number;
  imageUrl: string;
}

@Injectable({ providedIn: 'root' })
export class ActivityService {
  private readonly apiUrl = `${environment.apiUrl}/activities`;

  constructor(private http: HttpClient) {}

  getActivities(): Observable<Activity[]> {
    return this.http.get<Activity[]>(this.apiUrl);
  }

  getActivityById(id: number): Observable<Activity> {
    return this.http.get<Activity>(`${this.apiUrl}/${id}`);
  }

  getActivitiesByTripId(tripId: number): Observable<Activity[]> {
    return this.http.get<Activity[]>(`${this.apiUrl}/trip/${tripId}`);
  }
}
