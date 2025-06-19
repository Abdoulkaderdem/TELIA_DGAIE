import { TypeUser,RoleType } from './enumerations';

export interface User {
  id: number | null;
  firstName: string | null;
  lastName: string | null;
  email: string | null;
  password: string | null;
  matricule: string | null;
  typeUser: TypeUser | null;
  role: RoleType | null;
}