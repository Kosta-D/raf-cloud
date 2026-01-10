import {Component, OnInit} from "@angular/core";
import {ErrorLog} from "../../models/error-log.model";
import {ErrorLogService} from "../../services/error-log.service";
import {MachineService} from "../../services/machine.service";
import {AuthService} from "../../services/auth.service";
import {Machine} from "../../models/machine.model";


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
    const user: any = this.auth.getLoggedUser();

    this.machines.getAll().subscribe((all: Machine[]) => {

      const allowed: Machine[] = this.auth.isAdmin()
        ? all
        : all.filter(m => m.ownerUserId === user?.id);

      const map = new Map<number, string>(
        allowed.map(m => [m.id, m.name])
      );

      this.errors.getForMachines(allowed).subscribe((logs: ErrorLog[]) => {
        this.logs = logs.map(l => ({
          ...l,
          machineName: map.get(l.machineId)
        }));
      });

    });
  }
}
