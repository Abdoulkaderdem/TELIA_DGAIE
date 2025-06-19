import { RoleType } from './enum-roles';
import { EUserType } from './enum-userType';

export enum ACCOUNT_STATUS {
  ACTIVE = 'ACTIVE',
  DISABLE = 'DISABLE'
}

export interface UserInterface {
    id: number| null;
    firstName: string| null;
    lastName: string| null;
    email: string| null;
    password: string| null;
    matricule: string| null;
    typeUser: EUserType | null;
    role: RoleType | null;
};

/*export interface UserProfileResponse {
  status: number;
  message: string;
  data: UserInterface;
};*/

export interface UserListInterface {
    firstName: string | null;
    lastName: string | null;
    email: string | null;
    phone: string | null;
};

export interface Authority {
  authority: string;
};

export interface UserProfileData {
  id: string | null;
  dateCreated: string | null;
  lastUpdated: string | null;
  firstName: string;
  lastName: string;
  email: string;
  password: string | null;
  matricule: string | null;
  ifuLandLord: string | null;
  canReceiveNewProjectEmail: boolean;
  actif: boolean;
  typeUser: string | null;
  role: string;
  ministerialStructure: string | null;
  enabled: boolean;
  username: string;
  authorities: Authority[];
  credentialsNonExpired: boolean;
  accountNonExpired: boolean;
  accountNonLocked: boolean;
};

export interface UserProfileResponse {
  status: number;
  message: string;
  data: UserProfileData;
};
