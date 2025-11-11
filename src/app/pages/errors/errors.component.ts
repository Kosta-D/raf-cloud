import { Component, OnInit } from '@angular/core';
import { ErrorLog } from '../../models/error-log.model';
import { Machine } from '../../models/machine.model';
import { ErrorLogService } from '../../services/error-log.service';
import { MachineService } from '../../services/machine.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-errors',
  templateUrl: './errors.component.html',
  styleUrls: ['./errors.component.scss']
})
export class ErrorsComponent implements OnInit {
  logs: (ErrorLog & { machineName?: string })[] = [];

  constructor(
    private errors: ErrorLogService,
    private machines: MachineService,
    public auth: AuthService
  ) {}

  ngOnInit(): void {
    const user = this.auth.getLoggedUser();
    const all = this.machines.getAll();

    const allowed: Machine[] = this.isAdmin()
      ? all
      : all.filter(m => m.ownerUserId === user?.id);

    const map = new Map(allowed.map(m => [m.id, m.name]));
    this.logs = this.errors.getForMachines(allowed).map(l => ({
      ...l,
      machineName: map.get(l.machineId)
    }));
  }

  private isAdmin(): boolean {
    const u = this.auth.getLoggedUser();
    if (!u) return false;
    return Array.isArray(u.permissions) && u.permissions.length >= 6;
  }
}
