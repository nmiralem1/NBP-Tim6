import { Component } from '@angular/core';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent {
  isEditing = false;

  userProfile = {
    firstName: 'John',
    lastName: 'Doe',
    email: 'john.doe@example.com',
    phone: '+1 (555) 123-4567',
    birthDate: '1990-05-15',
    password: '••••••••'
  };

  editData = { ...this.userProfile };

  toggleEdit(): void {
    if (this.isEditing) {
      // Save changes
      this.userProfile = { ...this.editData };
    } else {
      // Start editing
      this.editData = { ...this.userProfile };
    }
    this.isEditing = !this.isEditing;
  }

  cancel(): void {
    this.isEditing = false;
    this.editData = { ...this.userProfile };
  }
}
