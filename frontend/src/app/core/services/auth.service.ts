import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { environment } from '../../../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private baseUrl = `${environment.apiUrl}/auth`;
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
        // Clear local state immediately so the UI updates right away,
        // regardless of whether the backend call succeeds.
        localStorage.removeItem('currentUser');
        this.currentUserSubject.next(null);

        return this.http.post<any>(`${this.baseUrl}/logout`, {}, { withCredentials: true });
    }

    clearLocalAuth(): void {
        localStorage.removeItem('currentUser');
        this.currentUserSubject.next(null);
    }

    refreshToken(): Observable<any> {
        return this.http.post<any>(`${this.baseUrl}/refresh`, {}, { withCredentials: true });
    }

    isLoggedIn(): boolean {
        return !!this.currentUserSubject.value;
    }
}
