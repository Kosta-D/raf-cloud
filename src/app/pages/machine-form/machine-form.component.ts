import { Component } from '@angular/core';
import { MachineService } from '../../services/machine.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-machine-form',
  templateUrl: './machine-form.component.html',
  styleUrls: ['./machine-form.component.scss']
})
export class MachineFormComponent {

  // 🔹 POLJA KOJA KORISTIŠ U FORMULARU
  name: string = '';
  description?: string;
  type?: string;

  constructor(
    private machines: MachineService,
    protected auth: AuthService
  ) {}

  submit(): void {
    const trimmed: string = this.name?.trim();

    if (!trimmed) {
      return;
    }

    const user: any = this.auth.getLoggedUser();
    if (!user) {
      return;
    }

    this.machines.createMachine({
      name: trimmed,
      description: this.description,
      type: this.type
    }).subscribe({
      next: () => {
        console.log('Mašina kreirana');
        this.name = '';
        this.description = '';
        this.type = '';
      },
      error: err => console.error(err)
    });
  }
}
