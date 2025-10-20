export type Permission =
  | "CREATE_USER"
  | "READ_USER"
  | "UPDATE_USER"
  | "DELETE_USER";

export interface User {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  password: string;
  permissions: Permission[];
}
