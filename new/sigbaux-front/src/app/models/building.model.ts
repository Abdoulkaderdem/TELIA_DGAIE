export interface OfferAndCharacteristics {
  id: number;
  values: number;
  characteristics: {
    id: number;
    libCourt: string;
    libLong: string;
    unitPrice: number;
  };
  idBuilding: number;
}

export interface Building {
  id: number;
  typeBuilding: string;
  typePropertyTitle: string;
  buildingValue: string;
  otherInformation: string;
  region: string;
  province: string;
  commune: string;
  city: string;
  district: string;
  sector: string;
  section: string;
  ilot: string;
  plot: string;
  street: string;
  doornumber: string;
  geolocation: string;
  rentPrice: string;
  code: string;
  buildingArea: string | null;
  listOfferAndCharacteristicsDto: OfferAndCharacteristics[];
  listBuildingStanding: string[];
  status: string | null;
}

export interface SendRentalPrice {
  provisionalRentAmount: number;
}