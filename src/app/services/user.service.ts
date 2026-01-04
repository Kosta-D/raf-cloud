import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';

export type UserResponse = {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  permissions: string[];
};

export type CreateUserRequest = {
  firstName: string;
  lastName: string;
  email: string;
  password: string;
  permissions: string[];
};

export type UpdateUserRequest = {
  firstName: string;
  lastName: string;
  email: string;
  password?: string;
  permissions: string[];
};

@Injectable({ providedIn: 'root' })
export class UserService {
  constructor(private http: HttpClient) {}

  getAll(): Observable<UserResponse[]> {
    return this.http.get<UserResponse[]>(`${environment.apiUrl}/users`);
  }

  create(req: CreateUserRequest): Observable<UserResponse> {
    return this.http.post<UserResponse>(`${environment.apiUrl}/users`, req);
  }

  update(id: number, req: UpdateUserRequest): Observable<UserResponse> {
    return this.http.put<UserResponse>(`${environment.apiUrl}/users/${id}`, req);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${environment.apiUrl}/users/${id}`);
  }
}
