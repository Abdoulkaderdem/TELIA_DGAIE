export interface MinisterialStructure {
    id?: number;
    name?: string;
    domain?: string;
    phone?: string;
    email?: string;
    ministerInCharge?: string;
    nameMinistry?: string;
    code?: string;
}

export enum RentalStatus {
    AVAILABLE = 'AVAILABLE',
    RENTED = 'RENTED',
    DISABLED = 'DISABLED',
    ENABLE = 'ENABLE',
    INPROGRESS = 'INPROGRESS',
    CANCELED = 'CANCELED',
    VALIDATED = 'VALIDATED'
}

export interface CharacteristicsUsage {
    id?: number;
    dataType: string;
    values: string;
}

export interface RequestCharacteristics {
    id: number;
    values: number;
    characteristics: CharacteristicsUsage;
}

export interface TypeUsage {
    id?: number;
    libCourt: string;
    libLong: string;
}

export interface RentalRequest {
  id?: number;
  dateRequest?: string;
  description?: string;
  status?: RentalStatus;
  legalStatus?: string;
  motivationRequest?: string;
  structureCurrentPosition?: string;
  agentsNumber?: string;
  managersNumber?: string;
  desiredGeographicalLocation?: string;
  leasePortfolioMinistry?: string;
  buildingsOccupancyStatus?: string;
  buildingUsage?: TypeUsage[];
  structure?: MinisterialStructure;
  characteristics?: RequestCharacteristics[];
}