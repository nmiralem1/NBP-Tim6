import { Component } from '@angular/core';
import { NgForm, NgModel } from '@angular/forms';
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
  submitted = false;
  isSubmitting = false;

  constructor(private authService: AuthService, private router: Router) { }

  isFieldInvalid(field: NgModel | null): boolean {
    return !!field && Boolean(field.invalid) && (field.touched || this.submitted);
  }

  onSubmit(form: NgForm) {
    this.submitted = true;
    this.errorMessage = '';

    if (form.invalid) {
      Object.values(form.controls).forEach(control => control.markAsTouched());
      return;
    }

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
