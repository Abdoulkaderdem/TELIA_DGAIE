import { TypeLandLord, QualityApplicant } from './enumerations';

export interface LandLord {
  id: number;
  ifu: string;
  typeLandLord: TypeLandLord;
  qualityApplicant: QualityApplicant;
  firstname: string;
  lastname: string;
  companyName: string;
  bp: string;
  phoneNumber: string;
  whatsapp: string;
  emailAdress: string;
  residencePlace: string;
}
