import { RequestAndCharacteristics } from "./request-and-characteristics";

export interface BuildingForInspection {
    id: number;
    typePropertyTitle: string;
    buildingValue: string;
    buildingArea: string;
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
    listBuildingStanding: string[];
    status: string;
    zone: string;
    locality: string;
    typeBuilding: string;
    inspectionObservation: string;
}
