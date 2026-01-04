import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import {
  UserService,
  CreateUserRequest,
  UpdateUserRequest
} from '../../services/user.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-user-form',
  templateUrl: './user-form.component.html',
  styleUrls: ['./user-form.component.scss']
})
export class UserFormComponent implements OnInit {

  isEditMode = false;
  userId: number | null = null;

  user = {
    firstName: '',
    lastName: '',
    email: '',
    password: '',
    permissions: [] as string[]
  };

  allPermissions: string[] = [
    'READ_USER', 'CREATE_USER', 'UPDATE_USER', 'DELETE_USER',
    'SEARCH_MACHINE', 'CREATE_MACHINE', 'START_MACHINE', 'STOP_MACHINE', 'RESTART_MACHINE', 'DESTROY_MACHINE',
    'SCHEDULE_OPERATION', 'VIEW_ERROR_LOGS'
  ];

  errorMessage = '';

  constructor(
    private userService: UserService,
    private route: ActivatedRoute,
    private router: Router,
    public authService: AuthService
  ) {}

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.isEditMode = true;
      this.userId = Number(idParam);
      this.loadUserForEdit(this.userId);
    }
  }

  private loadUserForEdit(id: number): void {
    this.errorMessage = '';

    this.userService.getAll().subscribe({
      next: (users) => {
        const existingUser = users.find(u => u.id === id);
        if (!existingUser) {
          this.errorMessage = 'Корисник није пронађен.';
          return;
        }
        this.user.firstName = existingUser.firstName;
        this.user.lastName = existingUser.lastName;
        this.user.email = existingUser.email;
        this.user.permissions = existingUser.permissions ?? [];
        this.user.password = ''; // ne popunjavamo password
      },
      error: (err) => {
        if (err?.status === 401) this.errorMessage = 'Nije prijavljan (401).';
        else if (err?.status === 403) this.errorMessage = 'Nemas dozvolu za pregled (403).';
        else this.errorMessage = 'greska pri ucitavanju korisnika.';
      }
    });
  }

  togglePermission(permission: string): void {
    if (this.user.permissions.includes(permission)) {
      this.user.permissions = this.user.permissions.filter(p => p !== permission);
    } else {
      this.user.permissions = [...this.user.permissions, permission];
    }
  }

  saveUser(): void {
    this.errorMessage = '';

    if (!this.user.firstName || !this.user.lastName || !this.user.email) {
      this.errorMessage = 'Ime, prezime i imejl su obavezni.';
      return;
    }

    if (!this.isEditMode && !this.user.password) {
      this.errorMessage = 'Lozinka je obavezna pri kreiranju.';
      return;
    }

    if (!this.user.permissions || this.user.permissions.length === 0) {
      this.errorMessage = 'Izaberi bar jednu dozvolu.';
      return;
    }

    if (this.isEditMode && this.userId != null) {
      const req: UpdateUserRequest = {
        firstName: this.user.firstName,
        lastName: this.user.lastName,
        email: this.user.email,
        permissions: this.user.permissions,
        ...(this.user.password ? { password: this.user.password } : {})
      };

      this.userService.update(this.userId, req).subscribe({
        next: () => this.router.navigate(['/users']),
        error: (err) => {
          if (err?.status === 401) this.errorMessage = 'Nisi prijavljen (401).';
          else if (err?.status === 403) this.errorMessage = 'Nemas dozvolu za izmenu (403).';
          else if (err?.status === 409) this.errorMessage = 'Imejl vec postoji (409).';
          else this.errorMessage = 'Greska pri azuriranju korisnika.';
        }
      });

    } else {
      const req: CreateUserRequest = {
        firstName: this.user.firstName,
        lastName: this.user.lastName,
        email: this.user.email,
        password: this.user.password,
        permissions: this.user.permissions
      };

      this.userService.create(req).subscribe({
        next: () => this.router.navigate(['/users']),
        error: (err) => {
          if (err?.status === 401) this.errorMessage = 'Nisi prijavljan (401).';
          else if (err?.status === 403) this.errorMessage = 'Nemas dozvolu za kreiranje (403).';
          else if (err?.status === 409) this.errorMessage = 'Imejl vec postoji (409).';
          else this.errorMessage = 'Greska pri kreiranju korisnika.';
        }
      });
    }
  }

  cancel(): void {
    this.router.navigate(['/users']);
  }
}
