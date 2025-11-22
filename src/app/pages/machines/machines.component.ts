import { Component, OnInit } from '@angular/core';
import { Machine, MachineState } from '../../models/machine.model';
import { MachineService } from '../../services/machine.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-machines',
  templateUrl: './machines.component.html',
  styleUrls: ['./machines.component.scss']
})
export class MachinesComponent implements OnInit {
  name = '';
  type = ''; // za tip masine
  stateChoices: MachineState[] = ['Upaljena', 'Ugašena'];
  stateSelection: { [key in MachineState]: boolean } = {
    Upaljena: false,
    Ugašena: false
  };

  from = '';
  to = '';

  results: Machine[] = [];

  constructor(
    private machineService: MachineService,
    public auth: AuthService
  ) {}

  ngOnInit(): void {
    this.search();
  }

  private isAdmin(): boolean {
    const u = this.auth.getLoggedUser();
    if (!u) return false;
    return Array.isArray(u.permissions) && u.permissions.length >= 6;
  }

  search(): void {
    const selectedStates = (Object.keys(this.stateSelection) as MachineState[])
      .filter(s => this.stateSelection[s]);

    const user = this.auth.getLoggedUser();

    this.results = this.machineService.search({
      name: this.name || undefined,
      type: this.type || undefined, //za tip masine
      states: selectedStates.length ? selectedStates : undefined,
      from: this.from || undefined,
      to: this.to || undefined,
      ownerUserId: user?.id,
      isAdmin: this.isAdmin()
    });
  }

  clearFilters(): void {
    this.name = '';
    this.type = ''; //za tip masine
    this.from = '';
    this.to = '';
    this.stateSelection = { Upaljena: false, Ugašena: false };
    this.search();
  }
}
