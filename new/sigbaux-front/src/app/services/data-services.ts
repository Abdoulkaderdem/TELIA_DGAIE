import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
    providedIn: 'root',
})
export class DataService {

    constructor(private http: HttpClient) { }

    getRegions() {
        return this.http.get<any>('assets/data/regions.json')
            .toPromise()
            .then(res => res.data as any[])
			.then(data => data);
    }

	getProvinces() {
        return this.http.get<any>('assets/data/provinces.json')
            .toPromise()
            .then(res => res.data as any[])
			.then(data => data);
    }

	getDepartements() {
        return this.http.get<any>('assets/data/departements.json')
            .toPromise()
            .then(res => res.data as any[])
			.then(data => data);
    }

	getSecteurs() {
        return this.http.get<any>('assets/data/secteurs_villages.json')
            .toPromise()
            .then(res => res.data as any[])
			.then(data => data);
    }
}
