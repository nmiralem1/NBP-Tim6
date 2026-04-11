import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';

@Component({
    selector: 'app-navbar',
    templateUrl: './navbar.component.html',
    styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent {
    isMenuOpen = false;

    constructor(
        public authService: AuthService,
        private router: Router
    ) {}

    toggleMenu(): void {
        this.isMenuOpen = !this.isMenuOpen;
    }

    closeMenu(): void {
        this.isMenuOpen = false;
    }

    logout(): void {
        this.authService.logout();
        this.isMenuOpen = false;
        this.router.navigate(['/login']);
    }
}
