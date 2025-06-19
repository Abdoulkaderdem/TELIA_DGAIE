import { MinisterialStructure } from './ministerial-structure';
import { RentalStatus } from './enumerations';
import { TypeUsage } from './type-usage';
import { RequestAndCharacteristics } from './request-and-characteristics';

export interface RentalRequest {
  id: number | null;
  dateRequest: string;
  description: string;
  legalStatus: string;
  motivationRequest: string;
  structureCurrentPosition: string;
  agentsNumber: string;
  managersNumber: string;
  region: string;
  province: string;
  commune: string;
  city: string;
  district: string;
  sector: string;
  regionDesired: string;
  provinceDesired: string;
  communeDesired: string;
  cityDesired: string;
  districtDesired: string;
  sectorDesired: string;
  leasePortfolioMinistry: string;
  buildingsOccupancyStatus: string;
  status: string | null;
  listBuildingUsageDto: TypeUsage[];
  structure: MinisterialStructure | null;
  listRequestAndCharacteristics: RequestAndCharacteristics[];
  numberOfRoom: number,
  numberOfRoomMeeting: number,
  numberOfBathroom: number

}

export interface BuildingRequest {
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
  characteristics?: RequestAndCharacteristics[];
}