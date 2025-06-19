export interface DataListResponse<T> {
    next: string | null,
    previous: string | null,
    results: T[],
    count:number
}

export type PageDataMapping<T> = Map<number,DataListResponse<T>>;