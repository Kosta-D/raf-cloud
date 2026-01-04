import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { UserService, UserResponse } from '../../services/user.service';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.scss']
})
export class UsersComponent implements OnInit {
  users: UserResponse[] = [];
  errorMessage: string = '';

  constructor(
    private userService: UserService,
    private router: Router,
    public authService: AuthService
  ) {}

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(): void {
    this.errorMessage = '';

    this.userService.getAll().subscribe({
      next: (data) => (this.users = data),
      error: (err) => {
        if (err?.status === 401) this.errorMessage = 'Nisi prijavljen (401).';
        else if (err?.status === 403) this.errorMessage = 'Nemas dozvolu za pregled korisnika (403).';
        else this.errorMessage = 'Greska pri ucitavanju korisnika.';
      }
    });
  }

  editUser(id: number): void {
    this.router.navigate(['/users/edit', id]);
  }

  deleteUser(id: number): void {
    if (!confirm('Da li sigurno zelis da obrises ovog korisnika?')) return;

    this.userService.delete(id).subscribe({
      next: () => this.loadUsers(),
      error: (err) => {
        if (err?.status === 401) this.errorMessage = 'Nisi prijavljen (401).';
        else if (err?.status === 403) this.errorMessage = 'Nemas dozvolu za brisanje korisnika (403).';
        else this.errorMessage = 'Greska pri brisanju korisnika.';
      }
    });
  }

  goToAddUser(): void {
    this.router.navigate(['/users/new']);
  }
}
