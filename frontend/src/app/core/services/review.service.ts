import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface Review {
  id: number;
  userId: number;
  accommodationId?: number;
  activityId?: number;
  rating: number;
  note: string;
  createdAt: string;
  userName?: string;
  userLocation?: string;
}

export interface CreateReviewRequest {
  userId: number;
  accommodationId?: number;
  activityId?: number;
  rating: number;
  note: string;
}

@Injectable({
  providedIn: 'root'
})
export class ReviewService {
  private apiUrl = `${environment.apiUrl}/reviews`;

  constructor(private http: HttpClient) {}

  getAllReviews(): Observable<Review[]> {
    return this.http.get<Review[]>(this.apiUrl);
  }

  createReview(request: CreateReviewRequest): Observable<string> {
    return this.http.post(`${this.apiUrl}`, request, {
      responseType: 'text',
      withCredentials: true
    });
  }
  
}
