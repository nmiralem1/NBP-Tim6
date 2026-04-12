import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

import { LoginComponent } from './features/login/login.component';
import { RegisterComponent } from './features/register/register.component';
import { ProfileComponent } from './features/profile/profile.component';
import { HomeComponent } from './features/home/home.component';
import { DestinationsComponent } from './features/destinations/destinations.component';
import { DestinationDetailsComponent } from './features/destination-details/destination-details.component';
import { NavbarComponent } from './shared/navbar/navbar.component';
import { AccommodationsComponent } from './features/accommodations/accommodations.component';
import { AccommodationDetailsComponent } from './features/accommodation-details/accommodation-details.component';
import { BookComponent } from './features/book/book.component';
import { CoreModule } from './core/core.module';
import { ActivitiesComponent } from './features/activities/activities.component';
import { ActivityDetailsComponent } from './features/activity-details/activity-details.component';
import { TransportationComponent } from './features/transportation/transportation.component';
import { TripsComponent } from './features/trips/trips.component';
import { BookingsComponent } from './features/bookings/bookings.component';
import { TripDetailsComponent } from './features/trip-details/trip-details.component';

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    LoginComponent,
    RegisterComponent,
    ProfileComponent,
    HomeComponent,
    DestinationsComponent,
    DestinationDetailsComponent,
    AccommodationsComponent,
    AccommodationDetailsComponent,
    BookComponent,
    ActivitiesComponent,
    ActivityDetailsComponent,
    TransportationComponent,
    TripsComponent,
    BookingsComponent,
    TripDetailsComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    CoreModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
