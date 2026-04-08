import { Component, OnInit } from '@angular/core';

interface UserProfile {
  username: string;
  email: string;
  phone: string;
  password: string;
  memberSince: string;
}

interface Booking {
  id: number;
  title: string;
  type: 'Activity' | 'Accommodation' | 'Transportation' | 'Travel Plan';
  bookingDate: string;
  status: 'Confirmed' | 'Pending' | 'Completed';
  location: string;
  price: number;
  imageUrl?: string;
  reviewText?: string;
  reviewRating?: number;
}

@Component({
  selector: 'app-bookings',
  templateUrl: './bookings.component.html',
  styleUrls: ['./bookings.component.scss']
})
export class BookingsComponent implements OnInit {
  userProfile: UserProfile = {
    username: 'enna_user',
    email: 'enna@example.com',
    phone: '+387 61 123 456',
    password: '********',
    memberSince: '2025-09-12'
  };

  bookings: Booking[] = [];
  filteredBookings: Booking[] = [];

  searchTerm: string = '';
  selectedType: string = '';
  selectedStatus: string = '';

  bookingTypes: string[] = ['Activity', 'Accommodation', 'Transportation', 'Travel Plan'];
  bookingStatuses: string[] = ['Confirmed', 'Pending', 'Completed'];

  isReviewModalOpen: boolean = false;
  selectedBooking: Booking | null = null;
  reviewText: string = '';
  selectedStars: number = 0;

  dummyBookings: Booking[] = [
    {
      id: 1,
      title: 'Paris City Walking Tour',
      type: 'Activity',
      bookingDate: '2026-04-03',
      status: 'Confirmed',
      location: 'Paris, France',
      price: 45,
      imageUrl: 'assets/images/paris.jpg'
    },
    {
      id: 2,
      title: 'Hotel Lumiere Deluxe Room',
      type: 'Accommodation',
      bookingDate: '2026-04-01',
      status: 'Completed',
      location: 'Paris, France',
      price: 220,
      imageUrl: 'assets/images/paris.jpg',
      reviewText: 'Beautiful room and great service.',
      reviewRating: 5
    },
    {
      id: 3,
      title: 'Rome Airport Shuttle',
      type: 'Transportation',
      bookingDate: '2026-03-29',
      status: 'Pending',
      location: 'Rome, Italy',
      price: 30,
      imageUrl: 'assets/images/rome.jpg'
    },
    {
      id: 4,
      title: 'Barcelona Weekend Escape',
      type: 'Travel Plan',
      bookingDate: '2026-03-25',
      status: 'Confirmed',
      location: 'Barcelona, Spain',
      price: 480,
      imageUrl: 'assets/images/barcelona.jpg'
    },
    {
      id: 5,
      title: 'Sunset Cruise Barcelona',
      type: 'Activity',
      bookingDate: '2026-03-20',
      status: 'Completed',
      location: 'Barcelona, Spain',
      price: 70,
      imageUrl: 'assets/images/barcelona.jpg'
    }
  ];

  ngOnInit(): void {
    this.loadBookings();
  }

  loadBookings(): void {
    this.bookings = this.dummyBookings;
    this.filteredBookings = this.dummyBookings;
  }

  filterBookings(): void {
    const term = this.searchTerm.toLowerCase().trim();

    this.filteredBookings = this.bookings.filter(item => {
      const matchesSearch =
        !term ||
        item.title.toLowerCase().includes(term) ||
        item.location.toLowerCase().includes(term) ||
        item.type.toLowerCase().includes(term);

      const matchesType =
        !this.selectedType || item.type === this.selectedType;

      const matchesStatus =
        !this.selectedStatus || item.status === this.selectedStatus;

      return matchesSearch && matchesType && matchesStatus;
    });
  }

  resetFilters(): void {
    this.searchTerm = '';
    this.selectedType = '';
    this.selectedStatus = '';
    this.filteredBookings = this.bookings;
  }

  openReviewModal(booking: Booking): void {
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
      case 'Activity':
        return 'type-activity';
      case 'Accommodation':
        return 'type-accommodation';
      case 'Transportation':
        return 'type-transportation';
      case 'Travel Plan':
        return 'type-travel-plan';
      default:
        return '';
    }
  }

  getStatusClass(status: string): string {
    switch (status) {
      case 'Confirmed':
        return 'status-confirmed';
      case 'Pending':
        return 'status-pending';
      case 'Completed':
        return 'status-completed';
      default:
        return '';
    }
  }

  getStarArray(): number[] {
    return [1, 2, 3, 4, 5];
  }
}
