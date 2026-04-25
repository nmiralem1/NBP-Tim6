import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AccommodationDetailsItem, AccommodationService } from '../../core/services/accommodation.service';
import { BookingService, BookingCreatedResponse } from '../../core/services/booking.service';
import { PaymentService, PaymentMethod } from '../../core/services/payment.service';
import { UserService } from '../../core/services/user.service';
import { environment } from '../../../environments/environment';

declare var Stripe: any;

type Step = 'booking' | 'payment' | 'success';

@Component({
  selector: 'app-book',
  templateUrl: './book.component.html',
  styleUrls: ['./book.component.scss']
})
export class BookComponent implements OnInit {
  accommodationId = 0;

  accommodation: AccommodationDetailsItem | null = null;
  currentStep: Step = 'booking';

  // Booking form
  checkIn = '';
  checkOut = '';
  guests = 1;

  // Created booking result
  createdBooking: BookingCreatedResponse | null = null;

  // Payment
  paymentMethods: PaymentMethod[] = [];
  selectedPaymentMethodId: number | null = null;

  // Stripe
  stripe: any = null;
  cardElement: any = null;
  cardMounted = false;

  // State
  isLoadingBooking = false;
  isProcessingPayment = false;
  bookingError = '';
  paymentError = '';

  // User
  userId: number | null = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private accommodationService: AccommodationService,
    private bookingService: BookingService,
    private paymentService: PaymentService,
    private userService: UserService
  ) {}

  tripId: number | null = null;

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.accommodationId = Number(params.get('accommodationId'));
      this.loadAccommodation();
    });

    this.route.queryParamMap.subscribe(qp => {
      const t = qp.get('tripId');
      this.tripId = t ? Number(t) : null;

      const bookingId = qp.get('bookingId');
      const totalPrice = qp.get('totalPrice');
      if (bookingId && totalPrice) {
        this.createdBooking = {
          bookingId: Number(bookingId),
          totalPrice: Number(totalPrice),
          bookingReference: ''
        };
        this.currentStep = 'payment';
      }
    });

    this.loadPaymentMethods();
    this.loadUserId();
  }

  private loadAccommodation(): void {
    this.accommodationService.getAccommodationById(this.accommodationId).subscribe({
      next: acc => this.accommodation = acc,
      error: () => this.router.navigate(['/accommodations'])
    });
  }

  private loadPaymentMethods(): void {
    this.paymentService.getPaymentMethods().subscribe({
      next: methods => this.paymentMethods = methods,
      error: () => {}
    });
  }

  private loadUserId(): void {
    const stored = localStorage.getItem('currentUser');
    if (stored) {
      const user = JSON.parse(stored);
      this.userId = user.id ?? null;
    }
    if (!this.userId) {
      this.userService.getMyProfile().subscribe({
        next: profile => this.userId = profile.id ?? null,
        error: () => {}
      });
    }
  }

  get totalNights(): number {
    if (!this.checkIn || !this.checkOut) return 0;
    const diff = new Date(this.checkOut).getTime() - new Date(this.checkIn).getTime();
    return Math.max(0, Math.round(diff / (1000 * 60 * 60 * 24)));
  }

  get estimatedPrice(): number {
    if (!this.accommodation) return 0;
    return this.totalNights * this.accommodation.pricePerNight;
  }

  submitBooking(): void {
    if (!this.checkIn || !this.checkOut || this.guests < 1) {
      this.bookingError = 'Please fill in all required fields.';
      return;
    }
    if (this.totalNights <= 0) {
      this.bookingError = 'Check-out must be after check-in.';
      return;
    }
    if (!this.userId) {
      this.bookingError = 'You must be logged in to make a booking.';
      return;
    }

    this.isLoadingBooking = true;
    this.bookingError = '';

    this.bookingService.createBooking({
      userId: this.userId,
      accommodationId: this.accommodationId,
      checkIn: this.checkIn,
      checkOut: this.checkOut,
      guestsCount: this.guests,
      ...(this.tripId ? { tripId: this.tripId } : {})
    }).subscribe({
      next: result => {
        this.createdBooking = result;
        this.isLoadingBooking = false;
        this.currentStep = 'payment';
      },
      error: err => {
        this.bookingError = err?.error?.message || 'Failed to create booking. Please try again.';
        this.isLoadingBooking = false;
      }
    });
  }

  selectPaymentMethod(id: number): void {
    this.selectedPaymentMethodId = id;
    this.paymentError = '';

    if (this.isCardMethod(id)) {
      this.cardMounted = false;
      setTimeout(() => this.mountStripeCard(), 150);
    } else {
      this.unmountStripeCard();
    }
  }

  isCardMethod(id: number): boolean {
    const method = this.paymentMethods.find(m => m.id === id);
    if (!method) return false;
    const name = method.name.toLowerCase();
    return name.includes('card') || name.includes('credit') || name.includes('debit') || name.includes('stripe');
  }

  get selectedMethodName(): string {
    const method = this.paymentMethods.find(m => m.id === this.selectedPaymentMethodId);
    return method?.name ?? '';
  }

  private mountStripeCard(): void {
    if (this.cardMounted) return;
    const el = document.getElementById('card-element');
    if (!el) return;

    if (!this.stripe) {
      this.stripe = Stripe(environment.stripePublishableKey);
    }

    const elements = this.stripe.elements();
    this.cardElement = elements.create('card', {
      style: {
        base: {
          fontFamily: 'Inter, sans-serif',
          fontSize: '16px',
          color: '#1a1a2e',
          '::placeholder': { color: '#9ca3af' }
        },
        invalid: { color: '#ef4444' }
      }
    });
    this.cardElement.mount('#card-element');
    this.cardMounted = true;
  }

  private unmountStripeCard(): void {
    if (this.cardElement && this.cardMounted) {
      this.cardElement.unmount();
      this.cardMounted = false;
    }
  }

  async processPayment(): Promise<void> {
    if (!this.selectedPaymentMethodId) {
      this.paymentError = 'Please select a payment method.';
      return;
    }
    if (!this.createdBooking || !this.userId) return;

    this.isProcessingPayment = true;
    this.paymentError = '';

    const amountInCents = Math.round(this.createdBooking.totalPrice * 100);

    this.paymentService.createPaymentIntent({
      amount: amountInCents,
      currency: 'EUR',
      bookingId: this.createdBooking.bookingId,
      userId: this.userId,
      paymentMethodId: this.selectedPaymentMethodId
    }).subscribe({
      next: async (intentResponse) => {
        if (this.isCardMethod(this.selectedPaymentMethodId!)) {
          await this.confirmCardPayment(intentResponse.clientSecret);
        } else {
          await this.finalizePayment();
        }
      },
      error: err => {
        this.paymentError = err?.error?.message || 'Failed to initialize payment. Please try again.';
        this.isProcessingPayment = false;
      }
    });
  }

  private async confirmCardPayment(clientSecret: string): Promise<void> {
    if (!this.stripe || !this.cardElement) {
      this.paymentError = 'Card payment not initialized. Please refresh and try again.';
      this.isProcessingPayment = false;
      return;
    }

    const result = await this.stripe.confirmCardPayment(clientSecret, {
      payment_method: { card: this.cardElement }
    });

    if (result.error) {
      this.paymentError = result.error.message || 'Card payment failed.';
      this.isProcessingPayment = false;
    } else if (result.paymentIntent?.status === 'succeeded') {
      await this.finalizePayment();
    }
  }

  private async finalizePayment(): Promise<void> {
    if (!this.createdBooking || !this.userId || !this.selectedPaymentMethodId) return;

    this.paymentService.savePaymentRecord({
      bookingId: this.createdBooking.bookingId,
      userId: this.userId,
      paymentMethodId: this.selectedPaymentMethodId,
      amount: this.createdBooking.totalPrice
    }).subscribe({
      next: () => {
        this.isProcessingPayment = false;
        this.currentStep = 'success';
      },
      error: () => {
        this.isProcessingPayment = false;
        this.currentStep = 'success';
      }
    });
  }

  goToBookings(): void {
    this.router.navigate(['/bookings']);
  }

  goBack(): void {
    this.router.navigate(['/accommodations', this.accommodationId]);
  }

  getTodayDate(): string {
    return new Date().toISOString().split('T')[0];
  }

  getMethodIcon(name: string): string {
    const n = name.toLowerCase();
    if (n.includes('card') || n.includes('credit') || n.includes('debit') || n.includes('stripe')) return '💳';
    if (n.includes('paypal')) return '🅿';
    if (n.includes('bank') || n.includes('transfer')) return '🏦';
    if (n.includes('cash')) return '💵';
    return '💰';
  }
}
