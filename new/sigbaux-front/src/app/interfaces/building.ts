import { TypeBuilding,TypePropertyTitle,BuildingStanding } from './enumerations';

export interface Building {
  id: number | null;
  typeBuilding: TypeBuilding | null;
  typePropertyTitle: TypePropertyTitle | null;
  buildingStanding: BuildingStanding | null;
  buildingValue: string | null;
  otherInformation: string | null;
  region: string | null;
  province: string | null;
  commune: string | null;
  city: string | null;
  district: string | null;
  sector: string | null;
  section: string | null;
  ilot: string | null;
  plot: string | null;
  street: string | null;
  doornumber: string | null;
  geolocation: string | null;
  rentPrice: string | null;
  code: string | null;
}

export interface SendRentalPrice{
  provisionalRentAmount: number;
}
