import { RentalRequest } from "./rental-request";

export interface ValidationCommittee {
    id: number;
    name: string;
    members: CommitteeMember[];
    responsible: CommitteeMember;
    rentalRequests: RentalRequest[];
}

export interface CommitteeMember {
    id: number;
    firstName: string;
    lastName: string;
    matricule: string;
    phoneNumber: string;
    email: string;
    function: string;
    committee: ValidationCommittee;
}