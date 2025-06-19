export interface InvoiceDTO {
    id?: number;
    contractId: number;
    amount: number;
    dueDate: string;
    startInterval: string;
    endInterval: string;
    description?: string;
    invoiceReference: string;
}

export interface InvoiceResponse {
    id: number;
    contractId: number;
    amount: number;
    dueDate: string;
    description?: string;
    invoiceReference: string;
    status: string;
    filePath: string;
}
