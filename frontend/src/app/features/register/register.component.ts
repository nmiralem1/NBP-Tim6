import { Component } from '@angular/core';
import { NgForm, NgModel } from '@angular/forms';
import { AuthService } from '../../core/services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {
  registerData = {
    firstName: '',
    lastName: '',
    username: '',
    email: '',
    passwordHash: '',
    phone: ''
  };
  confirmPassword = '';
  acceptedTerms = false;
  errorMessage = '';
  successMessage = '';
  submitted = false;
  isSubmitting = false;

  private readonly namePattern = /^[A-Za-zÀ-ž' -]{2,50}$/;
  private readonly usernamePattern = /^[a-zA-Z0-9._-]{3,20}$/;
  private readonly phonePattern = /^\+?[0-9 ]{8,15}$/;
  private readonly emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]{2,}$/;
  private readonly passwordPattern = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).{8,}$/;

  constructor(private authService: AuthService, private router: Router) { }

  get passwordsDoNotMatch(): boolean {
    return this.confirmPassword.length > 0 && this.registerData.passwordHash !== this.confirmPassword;
  }

  isFieldInvalid(field: NgModel | null): boolean {
    return !!field && Boolean(field.invalid) && (field.touched || this.submitted);
  }

  onSubmit(form: NgForm) {
    this.submitted = true;
    this.errorMessage = '';
    this.successMessage = '';

    if (form.invalid || this.passwordsDoNotMatch || !this.acceptedTerms) {
      Object.values(form.controls).forEach(control => control.markAsTouched());
      return;
    }

    const payload = {
      ...this.registerData,
      firstName: this.registerData.firstName.trim(),
      lastName: this.registerData.lastName.trim(),
      username: this.registerData.username.trim(),
      email: this.registerData.email.trim().toLowerCase(),
      phone: this.registerData.phone.trim()
    };

    this.isSubmitting = true;

    this.authService.register(payload).subscribe({
      next: () => {
        this.isSubmitting = false;
        this.successMessage = 'Registration successful! Redirecting to login...';
        setTimeout(() => this.router.navigate(['/login']), 2000);
      },
      error: (err) => {
        this.isSubmitting = false;
        this.errorMessage = err.error?.message || 'Registration failed';
      }
    });
  }

  readonly validationPatterns = {
    name: this.namePattern.source,
    username: this.usernamePattern.source,
    phone: this.phonePattern.source,
    email: this.emailPattern.source,
    password: this.passwordPattern.source
  };
}
