import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Activity, ActivityService } from '../../core/services/activity.service';

@Component({
  selector: 'app-activities',
  templateUrl: './activities.component.html',
  styleUrls: ['./activities.component.scss']
})
export class ActivitiesComponent implements OnInit {
  activities: Activity[] = [];
  filteredActivities: Activity[] = [];
  isLoading = true;

  searchTerm = '';
  selectedDate = '';
  selectedType = '';
  maxPrice: number | null = null;

  activityTypes: string[] = [];

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private activityService: ActivityService
  ) {}

  ngOnInit(): void {
    this.activityService.getActivities().subscribe({
      next: activities => {
        this.activities = activities;
        this.activityTypes = [...new Set(activities.map(a => String(a.activityTypeId)))];
        this.isLoading = false;
        this.filterActivities();
      },
      error: () => { this.isLoading = false; }
    });

    this.route.queryParams.subscribe(params => {
      this.selectedDate = params['date'] || '';
      this.filterActivities();
    });
  }

  filterActivities(): void {
    const term = this.searchTerm.toLowerCase().trim();
    this.filteredActivities = this.activities.filter(item => {
      const matchesSearch = !term || item.name.toLowerCase().includes(term) || item.location.toLowerCase().includes(term);
      const matchesDate = !this.selectedDate || item.activityDate === this.selectedDate;
      const matchesType = !this.selectedType || String(item.activityTypeId) === this.selectedType;
      const matchesPrice = this.maxPrice === null || item.price <= this.maxPrice;
      return matchesSearch && matchesDate && matchesType && matchesPrice;
    });
  }

  resetFilters(): void {
    this.searchTerm = '';
    this.selectedDate = '';
    this.selectedType = '';
    this.maxPrice = null;
    this.filteredActivities = this.activities;
  }

  openActivityDetails(id: number): void {
    this.router.navigate(['/activities', id]);
  }
}
