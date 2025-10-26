import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { MachineService } from '../../services/machine.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-machine-form',
  templateUrl: './machine-form.component.html',
  styleUrls: ['./machine-form.component.scss']
})
export class MachineFormComponent {
  name = '';

  constructor(
    private machines: MachineService,
    public auth: AuthService,
    private router: Router
  ) {}

  submit(): void {
    if (!this.auth.hasPermission('CREATE_MACHINE')) return;

    const trimmed = this.name.trim();
    if (!trimmed) {
      alert('Naziv je obavezan.');
      return;
    }

    const user = this.auth.getLoggedUser();
    if (!user) return;

    this.machines.create(trimmed, user.id);
    alert('Mašina je uspešno kreirana (Ugašena / Slobodna).');
    this.router.navigate(['/machines']);
  }
}
