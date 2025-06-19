import { RequestAndCharacteristics } from './request-and-characteristics';
import { TypeUsage } from './type-usage';

export interface LandLord {
    id: number | null;
    dateCreated: string | null;
    lastUpdated: string | null;
    ifu: string;
    typeLandLord: string;
    status: string;
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

export interface BuildingDto {
    id: number;
    typeBuilding: string;
    typePropertyTitle: string;
    status: string;
    buildingValue: string;
    buildingArea: string | null;
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
    listOfferAndCharacteristicsDto: RequestAndCharacteristics[];
    listLandlordOfferCharacteristics: RequestAndCharacteristics[];
    listBuildingOfferUsage: TypeUsage[];
    listBuildingStanding: string[];
}

export interface RentalOffer {
    id: number;
    dateOffer: string;
    rentalStatus: string;
    code: string;
    landLord: LandLord;
    buildingDtos: BuildingDto[];
}
