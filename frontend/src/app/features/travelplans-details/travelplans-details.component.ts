import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

interface TravelPlanAgendaItem {
  day: number;
  title: string;
  description: string;
  activities: string[];
}

interface TravelPlanDetails {
  id: number;
  name: string;
  cityName: string;
  countryName: string;
  durationDays: number;
  price: number;
  rating: number;
  transportationMode: string;
  departureLocation: string;
  arrivalLocation: string;
  availableFrom: string;
  availableTo: string;
  imageUrl: string;
  shortDescription: string;
  included: string[];
  agenda: TravelPlanAgendaItem[];
}

@Component({
  selector: 'app-travelplans-details',
  templateUrl: './travelplans-details.component.html',
  styleUrls: ['./travelplans-details.component.scss']
})
export class TravelplansDetailsComponent implements OnInit {
  travelPlan: TravelPlanDetails | null = null;

  bookingInfo = {
    departure: '',
    destination: '',
    departureDate: '',
    returnDate: '',
    travelers: 1
  };

  departureDateOptions: string[] = [];
  returnDateOptions: string[] = [];

  dummyTravelPlans: TravelPlanDetails[] = [
    {
      id: 1,
      name: 'Romantic Paris Getaway',
      cityName: 'Paris',
      countryName: 'France',
      durationDays: 3,
      price: 420,
      rating: 4.8,
      transportationMode: 'Flight',
      departureLocation: 'Sarajevo',
      arrivalLocation: 'Paris',
      availableFrom: '2026-04-10',
      availableTo: '2026-05-25',
      imageUrl: 'assets/images/paris.jpg',
      shortDescription: 'A perfect short Paris escape with a mix of sightseeing, relaxation and iconic city experiences.',
      included: ['Hotel included', 'Breakfast', 'City tour', 'Airport transfer'],
      agenda: [
        {
          day: 1,
          title: 'Arrival and City Center Walk',
          description: 'Arrival in Paris, hotel check-in and relaxed exploration of the city center.',
          activities: ['Airport transfer', 'Hotel check-in', 'Evening city walk']
        },
        {
          day: 2,
          title: 'Landmarks and Local Experience',
          description: 'Full day discovering famous Paris landmarks and enjoying local cuisine.',
          activities: ['Eiffel Tower area', 'Seine walk', 'Optional museum visit']
        },
        {
          day: 3,
          title: 'Free Time and Departure',
          description: 'Free morning for shopping or coffee before transfer to the airport.',
          activities: ['Free morning', 'Check-out', 'Airport transfer']
        }
      ]
    },
    {
      id: 2,
      name: 'Paris Culture Escape',
      cityName: 'Paris',
      countryName: 'France',
      durationDays: 5,
      price: 650,
      rating: 4.9,
      transportationMode: 'Flight',
      departureLocation: 'Sarajevo',
      arrivalLocation: 'Paris',
      availableFrom: '2026-04-12',
      availableTo: '2026-05-30',
      imageUrl: 'assets/images/paris.jpg',
      shortDescription: 'A longer Paris stay focused on museums, culture and central accommodation.',
      included: ['Museum pass', 'Central hotel', 'Breakfast', 'Airport transfer'],
      agenda: [
        {
          day: 1,
          title: 'Arrival and Check-in',
          description: 'Arrival in Paris and smooth hotel check-in.',
          activities: ['Flight arrival', 'Transfer', 'Hotel check-in']
        },
        {
          day: 2,
          title: 'Museum Day',
          description: 'Explore the most famous museums and cultural highlights.',
          activities: ['Louvre visit', 'Museum pass access', 'Local dinner']
        },
        {
          day: 3,
          title: 'Historic Paris',
          description: 'Discover historic streets, architecture and famous spots.',
          activities: ['Notre-Dame area', 'Latin Quarter', 'City walk']
        },
        {
          day: 4,
          title: 'Free Exploration',
          description: 'A flexible day for shopping, cafés or personal plans.',
          activities: ['Free time', 'Optional cruise', 'Evening in Montmartre']
        },
        {
          day: 5,
          title: 'Departure',
          description: 'Transfer to airport and return.',
          activities: ['Breakfast', 'Check-out', 'Airport transfer']
        }
      ]
    },
    {
      id: 3,
      name: 'Rome Weekend Adventure',
      cityName: 'Rome',
      countryName: 'Italy',
      durationDays: 3,
      price: 390,
      rating: 4.6,
      transportationMode: 'Flight',
      departureLocation: 'Sarajevo',
      arrivalLocation: 'Rome',
      availableFrom: '2026-04-15',
      availableTo: '2026-05-20',
      imageUrl: 'assets/images/rome.jpg',
      shortDescription: 'A compact Rome plan with highlights, good food and guided experiences.',
      included: ['Guided tour', 'Breakfast', 'Historic center stay'],
      agenda: [
        {
          day: 1,
          title: 'Arrival in Rome',
          description: 'Arrival, check-in and evening walk through the historic center.',
          activities: ['Hotel check-in', 'Historic center walk', 'Dinner']
        },
        {
          day: 2,
          title: 'Rome Highlights',
          description: 'Explore the city’s most famous historic attractions.',
          activities: ['Colosseum area', 'Trevi Fountain', 'Spanish Steps']
        },
        {
          day: 3,
          title: 'Departure Day',
          description: 'Enjoy breakfast and departure.',
          activities: ['Breakfast', 'Check-out', 'Transfer']
        }
      ]
    },
    {
      id: 4,
      name: 'Classic Rome Holiday',
      cityName: 'Rome',
      countryName: 'Italy',
      durationDays: 6,
      price: 780,
      rating: 4.7,
      transportationMode: 'Flight',
      departureLocation: 'Sarajevo',
      arrivalLocation: 'Rome',
      availableFrom: '2026-04-18',
      availableTo: '2026-05-28',
      imageUrl: 'assets/images/rome.jpg',
      shortDescription: 'A complete Rome holiday with iconic sights, more free time and comfortable accommodation.',
      included: ['Hotel included', 'Breakfast', 'Colosseum entry', 'Free day'],
      agenda: [
        {
          day: 1,
          title: 'Arrival and Check-in',
          description: 'Transfer and hotel accommodation.',
          activities: ['Arrival', 'Transfer', 'Hotel check-in']
        },
        {
          day: 2,
          title: 'Ancient Rome',
          description: 'Visit some of the most famous sites from ancient Rome.',
          activities: ['Colosseum', 'Forum area', 'Guided walk']
        },
        {
          day: 3,
          title: 'City Discovery',
          description: 'Relaxed discovery of central Rome.',
          activities: ['Trevi Fountain', 'Pantheon', 'Piazza Navona']
        },
        {
          day: 4,
          title: 'Free Day',
          description: 'A full day for personal exploration.',
          activities: ['Shopping', 'Cafés', 'Optional excursions']
        },
        {
          day: 5,
          title: 'Food and Culture',
          description: 'Enjoy local food and classic Roman neighborhoods.',
          activities: ['Local food experience', 'Trastevere', 'Evening walk']
        },
        {
          day: 6,
          title: 'Departure',
          description: 'End of trip and departure.',
          activities: ['Breakfast', 'Check-out', 'Transfer']
        }
      ]
    },
    {
      id: 5,
      name: 'Barcelona Summer Plan',
      cityName: 'Barcelona',
      countryName: 'Spain',
      durationDays: 4,
      price: 560,
      rating: 4.5,
      transportationMode: 'Flight',
      departureLocation: 'Sarajevo',
      arrivalLocation: 'Barcelona',
      availableFrom: '2026-04-20',
      availableTo: '2026-05-26',
      imageUrl: 'assets/images/barcelona.jpg',
      shortDescription: 'A sunny Barcelona plan combining beach time and city highlights.',
      included: ['Beach stay', 'Breakfast', 'City highlights'],
      agenda: [
        {
          day: 1,
          title: 'Arrival and Beach Walk',
          description: 'Arrival in Barcelona and a relaxed beachside evening.',
          activities: ['Transfer', 'Hotel check-in', 'Beach walk']
        },
        {
          day: 2,
          title: 'Barcelona Highlights',
          description: 'Visit the city’s most famous landmarks.',
          activities: ['Sagrada Familia area', 'City walk', 'Local food']
        },
        {
          day: 3,
          title: 'Free Exploration',
          description: 'Free day for shopping, cafés or optional tours.',
          activities: ['Free time', 'Optional cruise', 'Evening stroll']
        },
        {
          day: 4,
          title: 'Departure',
          description: 'End of trip.',
          activities: ['Breakfast', 'Check-out', 'Airport transfer']
        }
      ]
    },
    {
      id: 6,
      name: 'Barcelona Premium Escape',
      cityName: 'Barcelona',
      countryName: 'Spain',
      durationDays: 8,
      price: 980,
      rating: 4.8,
      transportationMode: 'Flight',
      departureLocation: 'Sarajevo',
      arrivalLocation: 'Barcelona',
      availableFrom: '2026-04-22',
      availableTo: '2026-05-30',
      imageUrl: 'assets/images/barcelona.jpg',
      shortDescription: 'A premium Barcelona plan with extended stay, better hotel and more flexibility.',
      included: ['Premium hotel', 'Airport transfer', 'Cruise option', 'Breakfast'],
      agenda: [
        {
          day: 1,
          title: 'Arrival',
          description: 'Arrival and premium hotel check-in.',
          activities: ['Arrival', 'Transfer', 'Hotel check-in']
        },
        {
          day: 2,
          title: 'City Center Discovery',
          description: 'Explore central Barcelona and its atmosphere.',
          activities: ['City center walk', 'Local markets', 'Dinner']
        },
        {
          day: 3,
          title: 'Architecture Day',
          description: 'Visit famous architecture and landmarks.',
          activities: ['Gaudí highlights', 'Photo stops', 'Museum option']
        },
        {
          day: 4,
          title: 'Beach Day',
          description: 'Relax and enjoy the coast.',
          activities: ['Beach time', 'Lunch', 'Evening walk']
        },
        {
          day: 5,
          title: 'Optional Cruise',
          description: 'Optional sea experience and relaxed afternoon.',
          activities: ['Cruise option', 'Port area', 'Free evening']
        },
        {
          day: 6,
          title: 'Free Day',
          description: 'Personal exploration and shopping.',
          activities: ['Shopping', 'Cafés', 'Free time']
        },
        {
          day: 7,
          title: 'Final City Experience',
          description: 'One more full city day before departure.',
          activities: ['Favorite spots revisit', 'Dinner', 'Packing']
        },
        {
          day: 8,
          title: 'Departure',
          description: 'Return transfer and departure.',
          activities: ['Breakfast', 'Check-out', 'Airport transfer']
        }
      ]
    }
  ];

  constructor(
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.loadTravelPlan(id);

    this.route.queryParams.subscribe(params => {
      this.bookingInfo.departure = params['departure'] || '';
      this.bookingInfo.destination = params['destination'] || this.travelPlan?.cityName || '';
      this.bookingInfo.departureDate = params['date'] || '';
      this.bookingInfo.returnDate = params['returnDate'] || '';
      this.bookingInfo.travelers = Number(params['travelers']) || 1;

      this.generateDateOptions();
      this.applyDefaultDates();
    });
  }

  loadTravelPlan(id: number): void {
    const foundPlan = this.dummyTravelPlans.find(item => item.id === id);
    this.travelPlan = foundPlan || null;

    if (this.travelPlan && !this.bookingInfo.destination) {
      this.bookingInfo.destination = this.travelPlan.cityName;
    }
  }

  generateDateOptions(): void {
    if (!this.travelPlan) {
      this.departureDateOptions = [];
      this.returnDateOptions = [];
      return;
    }

    const dates: string[] = [];
    const current = new Date(this.travelPlan.availableFrom);
    const end = new Date(this.travelPlan.availableTo);

    while (current <= end) {
      dates.push(this.formatDate(current));
      current.setDate(current.getDate() + 1);
    }

    this.departureDateOptions = dates;
    this.updateReturnDateOptions();
  }

  updateReturnDateOptions(): void {
    if (!this.travelPlan || !this.bookingInfo.departureDate) {
      this.returnDateOptions = [];
      return;
    }

    const departure = new Date(this.bookingInfo.departureDate);
    const minReturn = new Date(departure);
    minReturn.setDate(minReturn.getDate() + Math.max(this.travelPlan.durationDays - 1, 0));

    this.returnDateOptions = this.departureDateOptions.filter(date => {
      return new Date(date) >= minReturn;
    });
  }

  onDepartureDateChange(): void {
    this.updateReturnDateOptions();

    if (
      this.bookingInfo.returnDate &&
      !this.returnDateOptions.includes(this.bookingInfo.returnDate)
    ) {
      this.bookingInfo.returnDate = '';
    }

    if (!this.bookingInfo.returnDate && this.returnDateOptions.length > 0) {
      this.bookingInfo.returnDate = this.returnDateOptions[0];
    }
  }

  applyDefaultDates(): void {
    if (!this.travelPlan || this.departureDateOptions.length === 0) {
      return;
    }

    if (
      !this.bookingInfo.departureDate ||
      !this.departureDateOptions.includes(this.bookingInfo.departureDate)
    ) {
      this.bookingInfo.departureDate = this.departureDateOptions[0];
    }

    this.updateReturnDateOptions();

    if (
      !this.bookingInfo.returnDate ||
      !this.returnDateOptions.includes(this.bookingInfo.returnDate)
    ) {
      this.bookingInfo.returnDate = this.returnDateOptions.length > 0 ? this.returnDateOptions[0] : '';
    }
  }

  formatDate(date: Date): string {
    return date.toISOString().split('T')[0];
  }

  bookTravelPlan(): void {
    if (!this.travelPlan) {
      return;
    }

    alert(`Booking confirmed for ${this.travelPlan.name}`);
  }

  goBack(): void {
    this.router.navigate(['/travelplans'], {
      queryParams: {
        city: this.travelPlan?.cityName || '',
        country: this.travelPlan?.countryName || '',
        date: this.bookingInfo.departureDate || '',
        departure: this.bookingInfo.departure || '',
        destination: this.bookingInfo.destination || '',
        returnDate: this.bookingInfo.returnDate || '',
        travelers: this.bookingInfo.travelers || 1
      }
    });
  }
}
