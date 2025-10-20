import { Injectable } from '@angular/core';
import { User, Permission } from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private users: User[] = [
    {
      id: 1,
      firstName: 'Kosta',
      lastName: 'Dopudja',
      email: 'kosta@example.com',
      password: '12345',
      permissions: ['CREATE_USER', 'READ_USER', 'UPDATE_USER', 'DELETE_USER']
    },
    {
      id: 2,
      firstName: 'Mina',
      lastName: 'Protic',
      email: 'mina@example.com',
      password: '54321',
      permissions: ['READ_USER']
    },
    {
      id: 3,
      firstName: 'Andrej',
      lastName: 'Dasic',
      email: 'andrej@example.com',
      password: '11111',
      permissions: ['READ_USER', 'UPDATE_USER']
    }
  ];

  getUsers(): User[] {
    return this.users;
  }

  getUserById(id: number): User | undefined {
    return this.users.find(u => u.id === id);
  }

  addUser(user: User): void {
    user.id = this.users.length + 1;
    this.users.push(user);
  }

  updateUser(id: number, updatedUser: User): void {
    const index = this.users.findIndex(u => u.id === id);
    if (index !== -1) {
      this.users[index] = { ...updatedUser, id };
    }
  }

  deleteUser(id: number): void {
    this.users = this.users.filter(u => u.id !== id);
  }
}
