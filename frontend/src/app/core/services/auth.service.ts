import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, throwError } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private baseUrl = 'http://localhost:8080/api/auth';
    private currentUserSubject = new BehaviorSubject<any>(null);
    public currentUser = this.currentUserSubject.asObservable();

    constructor(private http: HttpClient) {
        // Try to restore user session from localStorage or cookie if needed
        const savedUser = localStorage.getItem('currentUser');
        if (savedUser) {
            this.currentUserSubject.next(JSON.parse(savedUser));
        }
    }

    login(credentials: any): Observable<any> {
        return this.http.post<any>(`${this.baseUrl}/login`, credentials, { withCredentials: true })
            .pipe(
                tap(user => {
                    localStorage.setItem('currentUser', JSON.stringify(user));
                    this.currentUserSubject.next(user);
                })
            );
    }

    register(user: any): Observable<any> {
        return this.http.post<any>(`${this.baseUrl}/register`, user);
    }

    logout(): Observable<any> {
        return this.http.post<any>(`${this.baseUrl}/logout`, {}, { withCredentials: true })
            .pipe(
                tap(() => {
                    localStorage.removeItem('currentUser');
                    this.currentUserSubject.next(null);
                })
            );
    }

    refreshToken(): Observable<any> {
        return this.http.post<any>(`${this.baseUrl}/refresh`, {}, { withCredentials: true });
    }

    isLoggedIn(): boolean {
        return !!this.currentUserSubject.value;
    }
}
