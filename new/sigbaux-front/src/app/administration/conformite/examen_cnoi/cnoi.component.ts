import { Component, OnInit } from '@angular/core';
import { TokenService } from 'src/app/services/token/token.service';
import { AuthenticationService } from 'src/app/services/auth/authentication.service';
import { MenuItem } from 'primeng/api';
@Component({
  selector: 'app-settings',
  templateUrl: './cnoi.component.html',
  styleUrls: ['./cnoi.component.css'],
})

export class CnoiComponent implements OnInit {
  items: MenuItem[] = [];

  cardMenu: MenuItem[] = [];

  constructor() { }

  ngOnInit() {
    this.items = [
        { label: 'Angular.io', icon: 'pi pi-external-link', url: 'http://angular.io' },
        { label: 'Theming', icon: 'pi pi-bookmark', routerLink: ['/theming'] }
    ];

    this.cardMenu = [
        {
            label: 'Save', icon: 'pi pi-fw pi-check'
        },
        {
            label: 'Update', icon: 'pi pi-fw pi-refresh'
        },
        {
            label: 'Delete', icon: 'pi pi-fw pi-trash'
        },
    ];
  }
}