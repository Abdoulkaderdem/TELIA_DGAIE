export interface BuildingUsage {
    id: number;
    libCourt: string;
    libLong: string;
  }
  
  export interface Structure {
    id: number;
    name: string;
    domain: string;
    phone: string;
    email: string;
    manager: string | null;
    code: string;
    idMinistry: number;
  }
  
  export interface Request {
    id: number;
    dateRequest: string;
    description: string;
    status: string;
    legalStatus: string;
    motivationRequest: string;
    structureCurrentPosition: string;
    agentsNumber: string;
    managersNumber: string;
    desiredGeographicalLocation: string;
    leasePortfolioMinistry: string;
    buildingsOccupancyStatus: string;
    listBuildingUsageDto: BuildingUsage[];
    structure: Structure;
    listCharacteristicsDto: any[];
  }
  