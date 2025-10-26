
export type MachinePermission =
  | 'SEARCH_MACHINE'
  | 'CREATE_MACHINE'
  | 'START_MACHINE'
  | 'STOP_MACHINE'
  | 'RESTART_MACHINE'
  | 'DESTROY_MACHINE';

export type MachineState = 'Slobodna' | 'Zauzeta';
export type PowerState = 'Upaljena' | 'Ugašena';


export interface Machine {
  id: number;
  name: string;
  description?: string;
  ownerUserId: number;
  createdAt: string;
  active: boolean;
  state: MachineState;
  power: PowerState;
  type?: string; // polje za tip masine
}
