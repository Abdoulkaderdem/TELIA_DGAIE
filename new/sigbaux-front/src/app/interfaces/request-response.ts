export interface DataResponse<T> {
    status: number;
    message: string;
    data: T;
}

export interface DataListResponse<T> {
    data: T[];
}