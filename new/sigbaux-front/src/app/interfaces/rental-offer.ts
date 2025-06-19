import { Building } from './building';
import { LandLord } from './landlord';
import { RentalStatus } from './enumerations';

export interface RentalOffer {
  id?: number;
  dateOffer: Date | null;
  rentalStatus: RentalStatus | null;
  code?: string;
  landLord?: LandLord | null;
  Buildings?: Building[];
}