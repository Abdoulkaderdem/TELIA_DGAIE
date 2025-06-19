import {EDocumentType} from "../interfaces/enum-documentType"

export class DocumentType {
    static Passport= { value: EDocumentType.PASSPORT, label: 'Passport' };
    static CNIB= { value: EDocumentType.CNIB, label: 'CNIB' };

    static getOptions() {
        return[DocumentType.CNIB, DocumentType.Passport];
    }
    
    getValue(val:string){

    }
}
