import { Observer,throwError } from "rxjs";
import { DataListResponse } from "../interfaces/data";
import { HttpErrorResponse } from '@angular/common/http';

export interface dataObserverProps {
    onSuccess: (response: any) => any,
    onFailure: (err: any) => any,
    onComplete?: () => any
}


export const getDataListObserver = <T>(args: dataObserverProps) => {
    const { onComplete, onFailure, onSuccess } = args;
    const dataListObserver: Observer<DataListResponse<T>> = {
        next: (response) => onSuccess(response),
        error: (error) => onFailure(error),
        complete: () => {
            if (onComplete != undefined) {
                onComplete()
            }
        }
    }
    return dataListObserver
}


export const getSingleDataObserver = <T>(args: dataObserverProps) => {
    const { onComplete, onFailure, onSuccess } = args;
    const dataObserver: Observer<T> = {
        next: (response) => onSuccess(response),
        error: (error) => onFailure(error),
        complete: () => {
            if (onComplete != undefined) {
                onComplete()
            }
        }
    }
    return dataObserver
}


export const createGenericObserver = <T>(callback: (data: T) => void): Observer<T> => {
    return {
        next: (response: T) => {
            callback(response);
        },
        error: (error: HttpErrorResponse) => {
            console.error('Error:', error);
            if (error.status === 302) {
                console.warn('Redirection detected, handling specific logic for status 302');
                if (error.error && Array.isArray(error.error)) {
                    callback(error.error as T);
                }
            } else {
                console.error(`Unhandled error status: ${error.status}`);
            }
            return throwError(() => error);
        },
        complete: () => { }
    }
}


export const updateGenericObserver = <T>(callback: (data: T) => void): Observer<T> => {
    return {
        next: (response: T) => {
            callback(response);
        },
        error: (error: HttpErrorResponse) => {
            console.error('Error:', error);
            if (error.status === 302) {
                console.warn('Redirection detected, handling specific logic for status 302');
                if (error.error && Array.isArray(error.error)) {
                    callback(error.error as T);
                }
            } else {
                console.error(`Unhandled error status: ${error.status}`);
            }
            return throwError(() => error);
        },
        complete: () => { }
    }
}