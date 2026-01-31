import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../core/services/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  username: string = '';
  email: string = '';
  password: string = '';

  constructor(private authService: AuthService) {
  }

  ngOnInit() {
  }

  onSubmit(): void {
    this.authService.register(this.username, this.email, this.password)
  }
}
