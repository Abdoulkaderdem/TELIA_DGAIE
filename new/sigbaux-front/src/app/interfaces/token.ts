import { EUserType } from "./enum-userType";

export interface IToken {
  access_token: string;
}

export interface TokenTypeUserInterface {
  key: string;
  userType: EUserType;
}

export interface TokenStatusInterface {
  status: string;
  answer: string;
}

export interface TokenResponse {
  access_token: string;
  expires_in: number;
  refresh_expires_in: number;
  refresh_token: string;
  token_type: string;
  not_before_policy: number;
  session_state: string;
  scope: string;
}


export interface TokenInterface {
  data: TokenStatusInterface;
  status: number;
}