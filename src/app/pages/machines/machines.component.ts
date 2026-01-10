import { Component, OnInit } from '@angular/core';
import { Machine } from '../../models/machine.model';
import { MachineService } from '../../services/machine.service';
import { AuthService } from '../../services/auth.service';
import { WebsocketService } from '../../services/websocket.service';


@Component({
  selector: 'app-machines',
  templateUrl: './machines.component.html',
  styleUrls: ['./machines.component.scss']
})
export class MachinesComponent implements OnInit {

  machines: Machine[] = [];

  // filteri
  name: string = '';
  type: string = '';
  from?: string;
  to?: string;

  // stanja za checkbox-e
  stateChoices: string[] = [
    'STOPPED',
    'RUNNING',
    'STARTING',
    'STOPPING',
    'RESTARTING'
  ];

  stateSelection: Record<string, boolean> = {};

  // rezultati pretrage
  results: Machine[] = [];

  constructor(
    private machineService: MachineService,
    public auth: AuthService,
    private websocketService: WebsocketService

  ) {}


  ngOnInit(): void {
    // inicijalno isključi sva stanja
    this.stateChoices.forEach(s => this.stateSelection[s] = false);

    // automatski učitaj sve mašine
    if (this.auth.hasPermission('SEARCH_MACHINE')) {
      this.search();
    }

    // =========================
    // 🔔 WEBSOCKET (DODATO)
    // =========================
    this.websocketService.connect();

    this.websocketService.onMachineUpdate().subscribe(updatedMachine => {
      const index = this.machines.findIndex(
        m => m.id === updatedMachine.id
      );

      if (index !== -1) {
        this.machines[index] = updatedMachine;
      }
    });
  }


  // ======================
  // PRETRAGA
  // ======================
  search(): void {
    const selectedStates = Object.keys(this.stateSelection)
      .filter(s => this.stateSelection[s]);

    this.machineService.searchMachines({
      name: this.name,
      type: this.type,
      from: this.from,
      to: this.to,
      states: selectedStates
    }).subscribe({
      next: data => this.results = data,
      error: err => console.error('Greška pri pretrazi mašina', err)
    });
  }

  clearFilters(): void {
    this.name = '';
    this.type = '';
    this.from = undefined;
    this.to = undefined;

    this.stateChoices.forEach(s => this.stateSelection[s] = false);

    this.search();
  }

  // ======================
  // AKCIJE NAD MAŠINAMA
  // ======================
  start(m: Machine): void {
    this.machineService.startMachine(m.id).subscribe({
      next: () => this.search(),
      error: err => console.error('Greška pri startovanju mašine', err)
    });
  }

  stop(m: Machine): void {
    this.machineService.stopMachine(m.id).subscribe({
      next: () => this.search(),
      error: err => console.error('Greška pri gašenju mašine', err)
    });
  }

  restart(m: Machine): void {
    this.machineService.restartMachine(m.id).subscribe({
      next: () => this.search(),
      error: err => console.error('Greška pri restartovanju mašine', err)
    });
  }

  destroy(m: Machine): void {
    if (!confirm(`Da li ste sigurni da želite da obrišete mašinu "${m.name}"?`)) {
      return;
    }

    this.machineService.deleteMachine(m.id).subscribe({
      next: () => this.search(),
      error: err => console.error('Greška pri brisanju mašine', err)
    });
  }

  // ======================
  // POMOĆNE METODE
  // ======================
  getStateLabel(state: string): string {
    switch (state) {
      case 'RUNNING': return 'Upaljena';
      case 'STOPPED': return 'Ugašena';
      case 'STARTING': return 'Pokreće se';
      case 'STOPPING': return 'Gasi se';
      case 'RESTARTING': return 'Restartuje se';
      default: return state;
    }
  }
}
