import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Machine } from '../models/machine.model';
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class MachineService {

  private readonly apiUrl = `${environment.apiUrl}/machines`;

  constructor(private http: HttpClient) {}

  // ======================
  // PRETRAGA MAŠINA
  // ======================
  searchMachines(filters: {
    name?: string;
    type?: string;
    from?: string;
    to?: string;
    states?: string[];
  }): Observable<Machine[]> {

    let params = new HttpParams();

    if (filters.name) {
      params = params.set('name', filters.name);
    }

    if (filters.type) {
      params = params.set('type', filters.type);
    }

    if (filters.from) {
      params = params.set('from', filters.from);
    }

    if (filters.to) {
      params = params.set('to', filters.to);
    }

    if (filters.states && filters.states.length > 0) {
      filters.states.forEach(state => {
        params = params.append('states', state);
      });
    }

    return this.http.get<Machine[]>(this.apiUrl, { params });
  }

  // ======================
  // KREIRANJE MAŠINE (BITNO!)
  // ======================
  create(machine: Partial<Machine>, userId: number): Observable<Machine> {
    return this.http.post<Machine>(this.apiUrl, {
      ...machine,
      ownerUserId: userId
    });
  }

  // (ostavljamo i ovu ako je koristiš negde drugde)
  createMachine(payload: {
    name: string;
    description?: string;
    type?: string;
  }): Observable<Machine> {
    return this.http.post<Machine>(this.apiUrl, payload);
  }

  getAll(): Observable<Machine[]> {
    return this.http.get<Machine[]>(this.apiUrl);
  }

  startMachine(id: number): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/${id}/start`, {});
  }

  stopMachine(id: number): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/${id}/stop`, {});
  }

  restartMachine(id: number): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/${id}/restart`, {});
  }

  deleteMachine(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
