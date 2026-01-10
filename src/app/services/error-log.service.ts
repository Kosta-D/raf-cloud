import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ErrorLog } from '../models/error-log.model';
import { Machine } from '../models/machine.model';
import {environment} from "../environments/environment";


@Injectable({
  providedIn: 'root'
})
export class ErrorLogService {

  private readonly apiUrl = `${environment.apiUrl}/error-logs`;

  constructor(private http: HttpClient) {}

  /**
   * Dohvata error logove za prosleđene mašine
   */
  getForMachines(machines: Machine[]): Observable<ErrorLog[]> {
    const ids: number[] = machines.map(m => m.id);
    return this.http.post<ErrorLog[]>(`${this.apiUrl}/machines`, ids);
  }
}
