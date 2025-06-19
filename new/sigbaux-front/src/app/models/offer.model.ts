// offer.model.ts
export interface LandLord {
    id: number;
    dateCreated: string;
    lastUpdated: string | null;
    ifu: string;
    typeLandLord: string;
    status: string | null;
    qualityApplicant: string;
    firstname: string;
    lastname: string;
    companyName: string;
    bp: string;
    phoneNumber: string;
    whatsapp: string;
    emailAdress: string;
    residencePlace: string;
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
    listOfferAndCharacteristicsDto: any[];
    listBuildingStanding: any[];
  }
  
  export interface Offer {
    id: number;
    dateOffer: string;
    rentalStatus: string;
    code: string;
    landLord: LandLord;
    buildingDtos: Building[];
  }  