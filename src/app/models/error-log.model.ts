
export type ScheduledOp = 'Upali' | 'Ugasi' | 'Restartuj';

export interface ErrorLog {
  id: number;
  machineId: number;
  when: string;
  operation: ScheduledOp;
  message: string;
}
