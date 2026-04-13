import { Component, OnInit } from '@angular/core';
import { NgForm, NgModel } from '@angular/forms';
import { UserService } from '../../core/services/user.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
  isEditing = false;
  submitted = false;
  errorMessage = '';
  successMessage = '';

  userProfile = {
    firstName: '',
    lastName: '',
    username: '',
    email: '',
    phone: '',
    role: ''
  };

  editData = {
    firstName: '',
    lastName: '',
    username: '',
    email: '',
    phone: ''
  };

  readonly validationPatterns = {
    name: "^[A-Za-zÀ-ž' -]{2,50}$",
    username: '^[a-zA-Z0-9._-]{3,20}$',
    email: '^[^\\s@]+@[^\\s@]+\\.[^\\s@]{2,}$',
    phone: '^\\+?[0-9 ]{8,15}$'
  };

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.loadProfile();
  }

  loadProfile(): void {
    this.userService.getMyProfile().subscribe({
      next: (user) => {
        this.userProfile = user;
        this.editData = {
          firstName: user.firstName || '',
          lastName: user.lastName || '',
          username: user.username || '',
          email: user.email || '',
          phone: user.phone || ''
        };
      },
      error: (err) => {
        console.error('Error loading profile:', err);
      }
    });
  }

  toggleEdit(): void {
    this.errorMessage = '';
    this.successMessage = '';
    this.submitted = false;
    this.editData = {
      firstName: this.userProfile.firstName || '',
      lastName: this.userProfile.lastName || '',
      username: this.userProfile.username || '',
      email: this.userProfile.email || '',
      phone: this.userProfile.phone || ''
    };
    this.isEditing = true;
  }

  isFieldInvalid(field: NgModel | null): boolean {
    return !!field && Boolean(field.invalid) && (field.touched || this.submitted);
  }

  saveChanges(form: NgForm): void {
    this.submitted = true;
    this.errorMessage = '';
    this.successMessage = '';

    if (form.invalid) {
      Object.values(form.controls).forEach(control => control.markAsTouched());
      return;
    }

    const payload = {
      firstName: this.editData.firstName.trim(),
      lastName: this.editData.lastName.trim(),
      username: this.editData.username.trim(),
      email: this.editData.email.trim().toLowerCase(),
      phone: this.editData.phone.trim()
    };

    this.userService.updateMyProfile(payload).subscribe({
      next: (updatedUser) => {
        this.userProfile = updatedUser;
        localStorage.setItem('currentUser', JSON.stringify(updatedUser));
        this.successMessage = 'Profile updated successfully.';
        this.isEditing = false;
      },
      error: (err) => {
        this.errorMessage = err.error?.message || 'Error updating profile.';
        console.error('Error updating profile:', err);
      }
    });
  }

  cancel(): void {
    this.isEditing = false;
    this.submitted = false;
    this.errorMessage = '';
  }
}
