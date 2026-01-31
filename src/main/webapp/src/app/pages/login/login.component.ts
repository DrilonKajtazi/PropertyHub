import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../core/services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  username: string = '';
  password: string = '';
  loginError: string = '';
  constructor(private authService: AuthService) {
  }

  ngOnInit(): void {
  }

  onSubmit(): void {
    this.authService.login(this.username, this.password).subscribe({
      error: (error) => {
       // Handle sign-in failure
       this.loginError = 'Invalid username or password. Please try again.';
       console.error('Sign-in failed:', error);
      }
    });
  }
}
