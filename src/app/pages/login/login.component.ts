import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  email: string = '';
  password: string = '';
  errorMessage: string = '';

  constructor(private authService: AuthService, private router: Router) {}

  onLogin(): void {
    const success = this.authService.login(this.email, this.password);
    if (success) {
      this.router.navigate(['/users']);
    } else {
      this.errorMessage = 'Погрешан имејл или лозинка!';
    }
  }
}
