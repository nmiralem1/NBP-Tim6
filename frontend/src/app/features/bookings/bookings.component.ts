import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { BookingDto, BookingService } from '../../core/services/booking.service';
import { ReviewService } from '../../core/services/review.service';

interface UserProfile {
  id?: number;
  username: string;
  email: string;
  phone?: string;
  firstName?: string;
  lastName?: string;
  role?: string;
}

interface BookingView {
  id: number;
  accommodationId: number;
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

  reviewError = '';
  isSubmittingReview = false;

  constructor(
    public authService: AuthService,
    private bookingService: BookingService,
    private reviewService: ReviewService,
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

  private get currentUserId(): number | null {
    const currentUser = localStorage.getItem('currentUser');

    if (!currentUser) {
      return null;
    }

    try {
      return JSON.parse(currentUser)?.id ?? null;
    } catch {
      return null;
    }
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
      error: () => {
        this.errorMessage = 'Error loading bookings.';
        this.isLoading = false;
      }
    });
  }

  mapBooking(item: BookingDto): BookingView {
    return {
      id: item.id,
      accommodationId: item.accommodationId,
      title: `Booking #${item.bookingReference || item.id}`,
      type: 'Accommodation',
      bookingDate: item.createdAt ? item.createdAt.split('T')[0] : '',
      status: (item.bookingStatus || '').toLowerCase(),
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
    this.reviewError = '';

    if (booking.status !== 'confirmed') {
      this.reviewError = 'You can leave a review only for confirmed bookings.';
      return;
    }

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
    this.reviewError = '';
    this.isSubmittingReview = false;
  }

  setStars(star: number): void {
    this.selectedStars = star;
  }

  submitReview(): void {
    if (!this.selectedBooking) return;

    this.reviewError = '';

    if (this.selectedBooking.status !== 'confirmed') {
      this.reviewError = 'You can leave a review only for confirmed bookings.';
      return;
    }

    if (this.selectedStars < 1) {
      this.reviewError = 'Please select a rating before submitting.';
      return;
    }

    if (!this.reviewText.trim()) {
      this.reviewError = 'Please write a review before submitting.';
      return;
    }

    const userId = this.currentUserId;

    if (!userId) {
      this.reviewError = 'User is not logged in.';
      return;
    }

    this.isSubmittingReview = true;

    this.reviewService.createReview({
      userId,
      accommodationId: this.selectedBooking.accommodationId,
      rating: this.selectedStars,
      note: this.reviewText.trim()
    }).subscribe({
      next: () => {
        const index = this.bookings.findIndex(
          b => b.id === this.selectedBooking?.id
        );

        if (index !== -1) {
          this.bookings[index].reviewText = this.reviewText.trim();
          this.bookings[index].reviewRating = this.selectedStars;
        }

        this.filterBookings();
        this.closeReviewModal();
      },
      error: (error) => {
        console.error('Review submit error:', error);
        this.reviewError =
          error?.error?.message ||
          error?.error ||
          'Failed to submit review.';
        this.isSubmittingReview = false;
      }
    });
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
