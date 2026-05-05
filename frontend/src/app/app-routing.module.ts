import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { HomeComponent } from './features/home/home.component';
import { LoginComponent } from './features/login/login.component';
import { RegisterComponent } from './features/register/register.component';
import { ProfileComponent } from './features/profile/profile.component';
import { DestinationsComponent } from './features/destinations/destinations.component';
import { DestinationDetailsComponent } from './features/destination-details/destination-details.component';
import { AccommodationsComponent } from './features/accommodations/accommodations.component';
import { AccommodationDetailsComponent } from './features/accommodation-details/accommodation-details.component';
import { BookComponent } from './features/book/book.component';
import { ActivitiesComponent } from './features/activities/activities.component';
import { ActivityDetailsComponent } from './features/activity-details/activity-details.component';
import { TransportationComponent } from './features/transportation/transportation.component';
import { TripsComponent } from './features/trips/trips.component';
import { BookingsComponent } from './features/bookings/bookings.component';
import { TripDetailsComponent } from './features/trip-details/trip-details.component';

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'profile', component: ProfileComponent },
  { path: 'destinations', component: DestinationsComponent },
  { path: 'destinations/:id', component: DestinationDetailsComponent },
  { path: 'accommodations', component: AccommodationsComponent },
  { path: 'accommodations/:id', component: AccommodationDetailsComponent },
  { path: 'book/transport/:id', component: BookComponent },
  { path: 'book/:accommodationId/:roomId', component: BookComponent },
  { path: 'activities', component: ActivitiesComponent },
  { path: 'activities/:id', component: ActivityDetailsComponent },
  { path: 'transportation', component: TransportationComponent },
  { path: 'trips', component: TripsComponent },
  { path: 'bookings', component: BookingsComponent },
  { path: 'trips/:id', component: TripDetailsComponent },
  { path: 'travelplans', redirectTo: 'trips', pathMatch: 'full' },
  { path: 'travelplans/:id', redirectTo: 'trips/:id', pathMatch: 'full' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
