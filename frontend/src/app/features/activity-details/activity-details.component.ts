import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Activity, ActivityService } from '../../core/services/activity.service';
import { Trip, TripService } from '../../core/services/trip.service';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-activity-details',
  templateUrl: './activity-details.component.html',
  styleUrls: ['./activity-details.component.scss']
})
export class ActivityDetailsComponent implements OnInit {
  activityId: number | null = null;
  activity: Activity | null = null;
  isLoading = true;

  myTrips: Trip[] = [];
  showTripModal = false;
  selectedTripId: number | null = null;
  isAddingToTrip = false;
  addToTripError = '';
  addToTripSuccess = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private activityService: ActivityService,
    private tripService: TripService,
    public authService: AuthService
  ) {}

  private get currentUserId(): number | null {
    const user = localStorage.getItem('currentUser');
    return user ? JSON.parse(user)?.id ?? null : null;
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const id = Number(params.get('id'));
      this.activityId = id;
      this.activityService.getActivityById(id).subscribe({
        next: activity => { this.activity = activity; this.isLoading = false; },
        error: () => { this.isLoading = false; this.router.navigate(['/activities']); }
      });
    });

    const userId = this.currentUserId;
    if (userId) {
      this.tripService.getTripsByUserId(userId).subscribe({
        next: trips => this.myTrips = trips,
        error: () => {}
      });
    }
  }

  goBack(): void {
    this.router.navigate(['/activities']);
  }

  openTripModal(): void {
    if (!this.authService.isLoggedIn()) {
      this.router.navigate(['/login']);
      return;
    }
    this.addToTripError = '';
    this.addToTripSuccess = '';
    this.selectedTripId = null;
    this.showTripModal = true;
  }

  confirmAddToTrip(): void {
    if (!this.selectedTripId) { this.addToTripError = 'Please select a trip.'; return; }
    if (!this.activity) return;

    this.isAddingToTrip = true;
    this.addToTripError = '';

    this.tripService.addActivityToTrip(this.selectedTripId, this.activity.id).subscribe({
      next: () => {
        this.isAddingToTrip = false;
        this.showTripModal = false;
        this.addToTripSuccess = 'Activity added to your trip!';
        setTimeout(() => this.addToTripSuccess = '', 3000);
      },
      error: (err) => {
        this.isAddingToTrip = false;
        this.addToTripError = err?.error || 'Failed to add activity to trip.';
      }
    });
  }
}
