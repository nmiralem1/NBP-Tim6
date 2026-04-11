import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

interface ActivityReview {
  id: number;
  author: string;
  rating: number;
  comment: string;
  date: string;
}

interface ActivityOption {
  id: number;
  title: string;
  duration: string;
  groupSize: string;
  price: number;
  available: boolean;
}

interface ActivityDetails {
  id: number;
  name: string;
  cityName: string;
  countryName: string;
  type: string;
  rating: number;
  price: number;
  description: string;
  imageUrl: string;
  gallery: string[];
  included: string[];
  availableFrom: string;
  availableTo: string;
  meetingPoint: string;
  duration: string;
  activityOptions: ActivityOption[];
  reviews: ActivityReview[];
}

@Component({
  selector: 'app-activity-details',
  templateUrl: './activity-details.component.html',
  styleUrls: ['./activity-details.component.scss']
})
export class ActivityDetailsComponent implements OnInit {
  activityId: number | null = null;
  activity: ActivityDetails | null = null;

  bookingForm = {
    activityDate: '',
    participants: 2
  };

  dummyActivities: ActivityDetails[] = [
    {
      id: 1,
      name: 'Paris City Walking Tour',
      cityName: 'Paris',
      countryName: 'France',
      type: 'Walking Tour',
      rating: 4.8,
      price: 45,
      description: 'Explore the most iconic parts of Paris with a local guide and discover famous landmarks, hidden streets, and the city’s unique atmosphere.',
      imageUrl: 'assets/images/paris.jpg',
      gallery: [
        'assets/images/paris.jpg',
        'assets/images/paris.jpg',
        'assets/images/paris.jpg'
      ],
      included: ['Local guide', 'Small group', 'Photo stops', 'Historic insights'],
      availableFrom: '2026-04-10',
      availableTo: '2026-05-20',
      meetingPoint: 'Central Paris Meeting Point',
      duration: '3 hours',
      activityOptions: [
        {
          id: 1,
          title: 'Morning Group Tour',
          duration: '3 hours',
          groupSize: 'Up to 12 people',
          price: 45,
          available: true
        },
        {
          id: 2,
          title: 'Private Afternoon Tour',
          duration: '3.5 hours',
          groupSize: 'Private',
          price: 90,
          available: true
        },
        {
          id: 3,
          title: 'Evening Highlights Tour',
          duration: '2.5 hours',
          groupSize: 'Up to 10 people',
          price: 50,
          available: false
        }
      ],
      reviews: [
        {
          id: 1,
          author: 'Emma R.',
          rating: 4.9,
          comment: 'Great guide and a lovely way to see the city.',
          date: '2026-03-18'
        },
        {
          id: 2,
          author: 'Daniel M.',
          rating: 4.7,
          comment: 'Very informative and well organized.',
          date: '2026-03-25'
        }
      ]
    },
    {
      id: 2,
      name: 'Louvre Museum Experience',
      cityName: 'Paris',
      countryName: 'France',
      type: 'Museum',
      rating: 4.9,
      price: 60,
      description: 'Visit the Louvre with an expert guide, avoid long waiting lines, and enjoy a curated tour through its most famous masterpieces.',
      imageUrl: 'assets/images/paris.jpg',
      gallery: [
        'assets/images/paris.jpg',
        'assets/images/paris.jpg',
        'assets/images/paris.jpg'
      ],
      included: ['Skip-the-line entry', 'Guide', 'Museum highlights'],
      availableFrom: '2026-04-12',
      availableTo: '2026-05-30',
      meetingPoint: 'Louvre Main Entrance',
      duration: '2.5 hours',
      activityOptions: [
        {
          id: 4,
          title: 'Standard Guided Visit',
          duration: '2.5 hours',
          groupSize: 'Up to 15 people',
          price: 60,
          available: true
        },
        {
          id: 5,
          title: 'Premium Small Group Visit',
          duration: '3 hours',
          groupSize: 'Up to 6 people',
          price: 95,
          available: true
        }
      ],
      reviews: [
        {
          id: 3,
          author: 'Sophia L.',
          rating: 4.9,
          comment: 'Fantastic experience, especially for first-time visitors.',
          date: '2026-04-02'
        }
      ]
    },
    {
      id: 3,
      name: 'Rome Street Food Tour',
      cityName: 'Rome',
      countryName: 'Italy',
      type: 'Food Experience',
      rating: 4.7,
      price: 55,
      description: 'Taste authentic Roman specialties while walking through vibrant neighborhoods with a local food expert.',
      imageUrl: 'assets/images/rome.jpg',
      gallery: [
        'assets/images/rome.jpg',
        'assets/images/rome.jpg',
        'assets/images/rome.jpg'
      ],
      included: ['Food tastings', 'Guide', 'Walking tour'],
      availableFrom: '2026-04-15',
      availableTo: '2026-05-18',
      meetingPoint: 'Piazza Navona',
      duration: '3 hours',
      activityOptions: [
        {
          id: 6,
          title: 'Classic Food Tour',
          duration: '3 hours',
          groupSize: 'Up to 10 people',
          price: 55,
          available: true
        },
        {
          id: 7,
          title: 'Evening Gourmet Tour',
          duration: '3.5 hours',
          groupSize: 'Up to 8 people',
          price: 75,
          available: true
        }
      ],
      reviews: [
        {
          id: 4,
          author: 'Lucas T.',
          rating: 4.6,
          comment: 'Delicious food and very fun atmosphere.',
          date: '2026-03-12'
        }
      ]
    }
  ];

  constructor(
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const id = Number(params.get('id'));
      this.activityId = id;
      this.loadActivityDetails(id);
    });

    this.route.queryParams.subscribe(params => {
      this.bookingForm.activityDate = params['date'] || '';
    });
  }

  loadActivityDetails(id: number): void {
    this.activity = this.dummyActivities.find(item => item.id === id) || null;

    if (!this.activity) {
      this.router.navigate(['/activities']);
      return;
    }
  }

  goBack(): void {
    this.router.navigate(['/activities']);
  }

  bookActivity(option: any): void {
    if (!option.available || !this.activity) return;

    this.router.navigate(['/book', this.activity.id, option.id]);
  }

  searchAvailability(): void {
    console.log('Activity availability search:', this.bookingForm);
  }
}
