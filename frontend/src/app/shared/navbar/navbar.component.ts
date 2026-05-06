import { Component, OnDestroy, OnInit } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { filter, Subscription } from 'rxjs';
import { AuthService } from '../../core/services/auth.service';
import { UserService } from '../../core/services/user.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit, OnDestroy {
  isMenuOpen = false;
  navbarProfileImageUrl: string | null = null;

  private routerSubscription?: Subscription;

  constructor(
    public authService: AuthService,
    private userService: UserService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadNavbarProfileImage();

    window.addEventListener('profileImageUpdated', this.handleProfileImageUpdated);

    this.routerSubscription = this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe(() => {
        this.loadNavbarProfileImage();
      });
  }

  ngOnDestroy(): void {
    window.removeEventListener('profileImageUpdated', this.handleProfileImageUpdated);
    this.routerSubscription?.unsubscribe();

    if (this.navbarProfileImageUrl?.startsWith('blob:')) {
      URL.revokeObjectURL(this.navbarProfileImageUrl);
    }
  }

  private handleProfileImageUpdated = (): void => {
    this.loadNavbarProfileImage();
  };

  loadNavbarProfileImage(): void {
    if (!this.authService.isLoggedIn()) {
      this.clearNavbarProfileImage();
      return;
    }

    this.userService.getProfileImage().subscribe({
      next: (blob) => {
        if (this.navbarProfileImageUrl?.startsWith('blob:')) {
          URL.revokeObjectURL(this.navbarProfileImageUrl);
        }

        this.navbarProfileImageUrl = URL.createObjectURL(blob);
      },
      error: () => {
        this.clearNavbarProfileImage();
      }
    });
  }

  clearNavbarProfileImage(): void {
    if (this.navbarProfileImageUrl?.startsWith('blob:')) {
      URL.revokeObjectURL(this.navbarProfileImageUrl);
    }

    this.navbarProfileImageUrl = null;
  }

  toggleMenu(): void {
    this.isMenuOpen = !this.isMenuOpen;
  }

  closeMenu(): void {
    this.isMenuOpen = false;
  }

  logout(): void {
    this.clearNavbarProfileImage();

    this.authService.logout().subscribe({
      next: () => {
        this.isMenuOpen = false;
        this.router.navigate(['/login']);
      },
      error: (err) => {
        console.error('Logout error:', err);
        this.isMenuOpen = false;
        this.router.navigate(['/login']);
      }
    });
  }
}
