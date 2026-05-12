import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface UserProfileDto {
  id?: number;
  firstName: string;
  lastName: string;
  username: string;
  email: string;
  phone: string;
  role: string;
}

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private readonly apiUrl = `${environment.apiUrl}/users`;

  constructor(private http: HttpClient) {}

  getMyProfile(): Observable<UserProfileDto> {
    return this.http.get<UserProfileDto>(`${this.apiUrl}/me`, {
      withCredentials: true
    });
  }

  updateMyProfile(data: {
    firstName: string;
    lastName: string;
    username: string;
    email: string;
    phone: string;
  }): Observable<UserProfileDto> {
    return this.http.put<UserProfileDto>(`${this.apiUrl}/me`, data, {
      withCredentials: true
    });
  }

  uploadProfileImage(file: File): Observable<string> {
    const formData = new FormData();
    formData.append('file', file);

    return this.http.post(`${this.apiUrl}/me/profile-image`, formData, {
      responseType: 'text',
      withCredentials: true
    });
  }

  getProfileImage(): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/me/profile-image`, {
      responseType: 'blob',
      withCredentials: true
    });
  }
}
