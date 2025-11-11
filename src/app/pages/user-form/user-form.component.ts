import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../../services/user.service';
import { User, Permission } from '../../models/user.model';

@Component({
  selector: 'app-user-form',
  templateUrl: './user-form.component.html',
  styleUrls: ['./user-form.component.scss']
})
export class UserFormComponent implements OnInit {

  user: User = {
    id: 0,
    firstName: '',
    lastName: '',
    email: '',
    password: '',
    permissions: [] as Permission[]
  };

  isEditMode = false;
  allPermissions: Permission[] = [

    'CREATE_USER', 'READ_USER', 'UPDATE_USER', 'DELETE_USER',

    'SEARCH_MACHINE', 'CREATE_MACHINE', 'START_MACHINE',
    'STOP_MACHINE', 'RESTART_MACHINE', 'DESTROY_MACHINE'

  ];

  constructor(
    private userService: UserService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.isEditMode = true;
      const id = Number(idParam);
      const existingUser = this.userService.getUserById(id);
      if (existingUser) {
        this.user = { ...existingUser };
      }
    }
  }

  togglePermission(permission: Permission): void {
    if (this.user.permissions.includes(permission)) {
      this.user.permissions = this.user.permissions.filter(p => p !== permission);
    } else {
      this.user.permissions.push(permission);
    }
  }

  saveUser(): void {
    // validacija
    if (!this.user.firstName || !this.user.lastName || !this.user.email || !this.user.password) {
      alert('Сва поља су обавезна!');
      return;
    }

    // provera duplikata email adrese (ako nije edit)
    const existing = this.userService.getUsers().find(u => u.email === this.user.email);
    if (existing && !this.isEditMode) {
      alert('Корисник са овим имејлом већ постоји!');
      return;
    }

    if (this.isEditMode) {
      this.userService.updateUser(this.user.id, this.user);
      alert('Подаци о кориснику су ажурирани!');
    } else {
      this.userService.addUser(this.user);
      alert('Нови корисник је успешно додат!');
    }

    this.router.navigate(['/users']);
  }

  cancel(): void {
    this.router.navigate(['/users']);
  }
}
