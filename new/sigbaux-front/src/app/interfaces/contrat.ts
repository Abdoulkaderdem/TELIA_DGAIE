export interface ContractDto {
    id?: number;
    buildingId: number;
    rentAmount: number;
    startDate: string;
    endDate: string | null;
    terms: string;
    contractPeriodicity: string;
    bankAccountNumber: string;
    president: string;
    reporter: string;
    contractingAuthority: string;

}

export interface ContractDtoEx {
    id?: number;
    buildingId: number;
    rentAmount: number;
    startDate: string;
    endDate: string | null;
    terms: string;
    contractPeriodicity: string;
    bankAccountNumber: string;
}

export interface ContractResponse {
    id: number;
    buildingId: number;
    rentAmount: number;
    startDate: string;
    endDate: string;
    terms: string;
    bankAccountNumber: string;
    president: string;
    reporter: string;
    contractingAuthority: string;
    contractPeriodicity: string;
    isRevised: boolean;
    isTerminated: boolean;
    status: string;
}

export interface ContractRevision {
    id: number;
    idContract: number;
    revisionDetails: string;
    revisionDate: string;
    status: string;
}

export interface ContractRevisionResponse {
    id: number;
    revisionDetails: string;
    revisionDate: string;
    status: string;
    idContract: number;
}
