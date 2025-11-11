import { Injectable } from '@angular/core';
import { ErrorLog, ScheduledOp } from '../models/error-log.model';
import { Machine } from '../models/machine.model';

@Injectable({ providedIn: 'root' })
export class ErrorLogService {
  private logs: ErrorLog[] = [
    {
      id: 1,
      machineId: 2,
      when: new Date(Date.now() - 1000 * 60 * 60 * 6).toISOString(),
      operation: 'Upali',
      message: 'Timeout on boot'
    },
    {
      id: 2,
      machineId: 3,
      when: new Date(Date.now() - 1000 * 60 * 30).toISOString(),
      operation: 'Restartuj',
      message: 'Agent not responding'
    }
  ];


  getAll(): ErrorLog[] {
    return [...this.logs];
  }


  getByMachineId(machineId: number): ErrorLog[] {
    return this.logs.filter(l => l.machineId === machineId);
  }


  getForMachines(machines: Machine[]): ErrorLog[] {
    const ids = new Set(machines.map(m => m.id));
    return this.logs.filter(l => ids.has(l.machineId));
  }

  addLog(entry: Omit<ErrorLog, 'id' | 'when'> & { when?: string }): ErrorLog {
    const id = this.logs.length ? Math.max(...this.logs.map(l => l.id)) + 1 : 1;
    const newLog: ErrorLog = {
      id,
      when: entry.when ?? new Date().toISOString(),
      machineId: entry.machineId,
      operation: entry.operation,
      message: entry.message
    };
    this.logs.unshift(newLog); // najnoviji prvi
    return newLog;
  }

  record(machineId: number, operation: ScheduledOp, message: string): ErrorLog {
    return this.addLog({ machineId, operation, message });
  }

  clear(): void {
    this.logs = [];
  }
}
