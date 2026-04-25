import { Component } from '@angular/core';
import { AuthService } from '../../core/services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  loginData = { username: '', password: '' };
  errorMessage = '';
  isSubmitting = false;

  constructor(private authService: AuthService, private router: Router) {}

  onSubmit() {
    this.errorMessage = '';
    this.isSubmitting = true;

    const payload = {
      username: this.loginData.username.trim(),
      password: this.loginData.password
    };

    this.authService.login(payload).subscribe({
      next: () => {
        this.isSubmitting = false;
        this.router.navigate(['/']);
      },
      error: (err) => {
        this.isSubmitting = false;
        const message = err.error?.message || 'Login failed';
        this.errorMessage =
          typeof message === 'string' && message.toLowerCase() === 'bad credentials'
            ? 'Bad credentials'
            : message;
      }
    });
  }
}
