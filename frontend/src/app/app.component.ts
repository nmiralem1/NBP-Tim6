import { Component, OnInit } from '@angular/core';
import { ApiService } from './core/services/api.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {

  countries: any[] = [];

  constructor(private apiService: ApiService) {}

  ngOnInit(): void {
    this.apiService.getCountries().subscribe({
      next: (data) => {
        console.log('Countries:', data);
        this.countries = data;
      },
      error: (err) => {
        console.error('Greška:', err);
      }
    });
  }
}