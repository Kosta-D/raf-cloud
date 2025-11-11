import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from './user.service';
@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private readonly TOKEN_KEY = 'authToken';
  private readonly USER_KEY = 'loggedUser';

  constructor(private router: Router,
              private userService: UserService
  ) {}

  login(email: string, password: string): boolean {
    const user = this.userService.getUsers().find(
      u => u.email === email && u.password === password
    );
    if (user) {
      const token = 'fake-jwt-token';
      localStorage.setItem(this.TOKEN_KEY, token);
      localStorage.setItem(this.USER_KEY, JSON.stringify(user));
      return true;
    }

    return false;
/*
    if (email === 'admin@example.com' && password === 'admin123') {
      const token = 'fake-jwt-token';
      const user = { email, permissions: ['CREATE_USER', 'READ_USER', 'UPDATE_USER', 'DELETE_USER'] };
      localStorage.setItem(this.TOKEN_KEY, token);
      localStorage.setItem(this.USER_KEY, JSON.stringify(user));
      return true;
    }
    return false;*/
  }

  logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.USER_KEY);
    this.router.navigate(['/login']);
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem(this.TOKEN_KEY);
  }


  getLoggedUser(): any {
    const user = localStorage.getItem(this.USER_KEY);
    return user ? JSON.parse(user) : null;
  }

  hasPermission(permission: string): boolean {
    const user = this.getLoggedUser();
    if (!user || !user.permissions) return false;
    return user.permissions.includes(permission);
  }

}
