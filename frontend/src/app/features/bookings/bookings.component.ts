import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { BookingDto, BookingService } from '../../core/services/booking.service';

interface UserProfile {
  username: string;
  email: string;
  phone?: string;
  firstName?: string;
  lastName?: string;
  role?: string;
}

interface BookingView {
  id: number;
  title: string;
  type: 'Accommodation';
  bookingDate: string;
  status: string;
  location: string;
  price: number;
  imageUrl?: string;
  reviewText?: string;
  reviewRating?: number;
  checkIn: string;
  checkOut: string;
  guestsCount: number;
  bookingReference: string;
}

@Component({
  selector: 'app-bookings',
  templateUrl: './bookings.component.html',
  styleUrls: ['./bookings.component.scss']
})
export class BookingsComponent implements OnInit {
  userProfile: UserProfile | null = null;

  bookings: BookingView[] = [];
  filteredBookings: BookingView[] = [];

  searchTerm = '';
  selectedType = '';
  selectedStatus = '';

  bookingTypes: string[] = ['Accommodation'];
  bookingStatuses: string[] = ['confirmed', 'pending', 'cancelled'];

  isReviewModalOpen = false;
  selectedBooking: BookingView | null = null;
  reviewText = '';
  selectedStars = 0;

  isLoading = false;
  errorMessage = '';

  constructor(
    public authService: AuthService,
    private bookingService: BookingService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const currentUser = localStorage.getItem('currentUser');

    if (!currentUser) {
      this.router.navigate(['/login']);
      return;
    }

    this.userProfile = JSON.parse(currentUser);
    this.loadBookings();
  }

  loadBookings(): void {
    this.isLoading = true;
    this.errorMessage = '';

    this.bookingService.getMyBookings().subscribe({
      next: (data: BookingDto[]) => {
        this.bookings = data.map(item => this.mapBooking(item));
        this.filteredBookings = [...this.bookings];
        this.isLoading = false;
      },
      error: (error) => {
        this.errorMessage = 'Error loading bookings.';
        this.isLoading = false;
      }
    });
  }

  mapBooking(item: BookingDto): BookingView {
    return {
      id: item.id,
      title: `Booking #${item.bookingReference || item.id}`,
      type: 'Accommodation',
      bookingDate: item.createdAt ? item.createdAt.split('T')[0] : '',
      status: item.bookingStatus || '',
      location: `Accommodation ID: ${item.accommodationId}`,
      price: item.totalPrice || 0,
      checkIn: item.checkIn,
      checkOut: item.checkOut,
      guestsCount: item.guestsCount,
      bookingReference: item.bookingReference || ''
    };
  }

  filterBookings(): void {
    const term = this.searchTerm.toLowerCase().trim();

    this.filteredBookings = this.bookings.filter(booking => {
      const matchesSearch =
        !term ||
        booking.title.toLowerCase().includes(term) ||
        booking.location.toLowerCase().includes(term) ||
        booking.bookingReference.toLowerCase().includes(term);

      const matchesType =
        !this.selectedType || booking.type === this.selectedType;

      const matchesStatus =
        !this.selectedStatus || booking.status === this.selectedStatus;

      return matchesSearch && matchesType && matchesStatus;
    });
  }

  resetFilters(): void {
    this.searchTerm = '';
    this.selectedType = '';
    this.selectedStatus = '';
    this.filteredBookings = [...this.bookings];
  }

  openReviewModal(booking: BookingView): void {
    this.selectedBooking = booking;
    this.reviewText = booking.reviewText || '';
    this.selectedStars = booking.reviewRating || 0;
    this.isReviewModalOpen = true;
  }

  closeReviewModal(): void {
    this.isReviewModalOpen = false;
    this.selectedBooking = null;
    this.reviewText = '';
    this.selectedStars = 0;
  }

  setStars(star: number): void {
    this.selectedStars = star;
  }

  submitReview(): void {
    if (!this.selectedBooking) return;

    const index = this.bookings.findIndex(b => b.id === this.selectedBooking?.id);

    if (index !== -1) {
      this.bookings[index].reviewText = this.reviewText;
      this.bookings[index].reviewRating = this.selectedStars;
    }

    this.filterBookings();
    this.closeReviewModal();
  }

  getTypeClass(type: string): string {
    switch (type) {
      case 'Accommodation':
        return 'type-accommodation';
      default:
        return '';
    }
  }

  getStatusClass(status: string): string {
    switch (status) {
      case 'confirmed':
        return 'status-confirmed';
      case 'pending':
        return 'status-pending';
      case 'cancelled':
        return 'status-cancelled';
      default:
        return '';
    }
  }

  getStarArray(): number[] {
    return [1, 2, 3, 4, 5];
  }
}