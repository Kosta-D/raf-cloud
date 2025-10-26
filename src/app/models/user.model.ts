import { MachinePermission } from './machine.model';

export type UserPermission =
  | 'READ_USER'
  | 'CREATE_USER'
  | 'UPDATE_USER'
  | 'DELETE_USER';

export type Permission = UserPermission | MachinePermission;

export interface User {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  password: string;
  permissions: Permission[];
}
