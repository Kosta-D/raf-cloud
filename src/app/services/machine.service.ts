import { Injectable } from '@angular/core';
import { Machine, MachineState } from '../models/machine.model';

export interface MachineSearchOptions {
  name?: string;
  type?: string; // za tip masine
  states?: MachineState[];
  from?: string;
  to?: string;
  ownerUserId?: number;
  isAdmin?: boolean;
}

@Injectable({ providedIn: 'root' })
export class MachineService {
  private machines: Machine[] = [
    {
      id: 1, name: 'Alpha', description: 'Build node', ownerUserId: 1,
      createdAt: new Date(Date.now() - 1000 * 60 * 60 * 24 * 7).toISOString(),
      active: true, state: 'Ugašena', type: 'Build'
    },
    {
      id: 2, name: 'Beta', description: 'DB server', ownerUserId: 2,
      createdAt: new Date(Date.now() - 1000 * 60 * 60 * 24 * 3).toISOString(),
      active: true, state: 'Upaljena', type: 'Database'
    },
    {
      id: 3, name: 'Gamma', description: 'CI runner', ownerUserId: 1,
      createdAt: new Date(Date.now() - 1000 * 60 * 60 * 24 * 2).toISOString(),
      active: true, state: 'Ugašena', type: 'CI'
    },
    {
      id: 4, name: 'Delta', description: 'Cache node', ownerUserId: 3,
      createdAt: new Date().toISOString(),
      active: true, state: 'Upaljena', type: 'Cache'
    },
    {
      id: 5, name: 'Epsilon', description: 'Analytics', ownerUserId: 2,
      createdAt: new Date().toISOString(),
      active: true, state: 'Ugašena', type: 'Analytics'
    },
    {
      id: 6, name: 'Zeta', description: 'Dev VM', ownerUserId: 1,
      createdAt: new Date().toISOString(),
      active: true, state: 'Ugašena', type: 'Dev'
    }
  ];



  getAll(): Machine[] {
    return [...this.machines];
  }

  getById(id: number): Machine | undefined {
    return this.machines.find(m => m.id === id);
  }

  create(name: string, ownerUserId: number, description = ''): Machine {
    const nextId = this.machines.length ? Math.max(...this.machines.map(m => m.id)) + 1 : 1;
    const newItem: Machine = {
      id: nextId,
      name: name.trim(),
      description,
      ownerUserId,
      createdAt: new Date().toISOString(),
      active: true,
      state: 'Ugašena'
    };
    this.machines.push(newItem);
    return newItem;
  }

  update(partial: Partial<Machine> & { id: number }): Machine | undefined {
    const idx = this.machines.findIndex(m => m.id === partial.id);
    if (idx === -1) return undefined;
    this.machines[idx] = { ...this.machines[idx], ...partial };
    return this.machines[idx];
  }



  search(opts: MachineSearchOptions = {}): Machine[] {
    const { name, type, states, from, to, ownerUserId, isAdmin } = opts;
    const fromDate = from ? new Date(from) : undefined;
    const toDate   = to   ? new Date(to)   : undefined;

    return this.machines.filter(m => {
      if (!m.active) return false;
      if (!isAdmin && ownerUserId != null && m.ownerUserId !== ownerUserId) return false;

      if (name && !m.name.toLowerCase().includes(name.toLowerCase())) return false;

      if (type && !(m.type ?? '').toLowerCase().includes(type.toLowerCase())) return false; // ⬅️ NOVO

      if (states && states.length > 0 && !states.includes(m.state)) return false;
      if (fromDate && new Date(m.createdAt) < fromDate) return false;
      if (toDate && new Date(m.createdAt) > toDate) return false;



      return true;
    });
  }

}
