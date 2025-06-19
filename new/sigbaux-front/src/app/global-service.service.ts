import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class GlobalService {

  isLogged: boolean = false;

  constructor() { }
}
