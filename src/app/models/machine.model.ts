
export type MachinePermission =
  | 'SEARCH_MACHINE'
  | 'CREATE_MACHINE'
  | 'START_MACHINE'
  | 'STOP_MACHINE'
  | 'RESTART_MACHINE'
  | 'DESTROY_MACHINE';

export type MachineState = 'Upaljena' | 'Ugašena';


export interface Machine {
  id: number;
  name: string;
  description?: string;
  ownerUserId: number;
  createdAt: string;
  active: boolean;
  state: MachineState;
  type?: string; // polje za tip masine
}
