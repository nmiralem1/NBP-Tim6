import { Component, OnInit } from '@angular/core';
import { UserService } from '../../core/services/user.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
  isEditing = false;

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
    this.editData = {
      firstName: this.userProfile.firstName || '',
      lastName: this.userProfile.lastName || '',
      username: this.userProfile.username || '',
      email: this.userProfile.email || '',
      phone: this.userProfile.phone || ''
    };
    this.isEditing = true;
  }

  saveChanges(): void {
    this.userService.updateMyProfile(this.editData).subscribe({
      next: (updatedUser) => {
        this.userProfile = updatedUser;
        localStorage.setItem('currentUser', JSON.stringify(updatedUser));
        this.isEditing = false;
      },
      error: (err) => {
        console.error('Error updating profile:', err);
      }
    });
  }

  cancel(): void {
    this.isEditing = false;
  }
}