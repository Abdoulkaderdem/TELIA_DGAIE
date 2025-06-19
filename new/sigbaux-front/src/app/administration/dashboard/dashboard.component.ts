import { Component, OnInit } from '@angular/core';
import { TokenService } from 'src/app/services/token/token.service';
import { AuthenticationService } from 'src/app/services/auth/authentication.service';
import { DashboardService, DashboardCountsResponse } from 'src/app/services/dashboard.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css'],
})

export class DashboardComponent implements OnInit {
  dashboardCounts: DashboardCountsResponse | null = null;
  cards: any[] = [];

  constructor(
    private tokenService: TokenService,
    private authService: AuthenticationService,
    private dashboardService: DashboardService
  ) { }

  ngOnInit() {
    this.tokenService.userAlwaysConnected();
    this.loadDashboardCounts();
  }

  loadDashboardCounts(): void {
    this.dashboardService.getDashboardCounts().subscribe(
      (data: DashboardCountsResponse) => {
        this.dashboardCounts = data;
        this.cards = [
            { title: 'Nombre de Bâtiments', count: this.dashboardCounts.buildingCount, icon: 'pi pi-list' },
            { title: 'Nombre de Demandes de Location', count: this.dashboardCounts.rentalRequestCount, icon: 'pi pi-file' },
            { title: 'Nombre d\'Offres de location', count: this.dashboardCounts.requestOfferCount, icon: 'pi pi-building' },
            { title: 'Nombre de Contrats en cours', count: this.dashboardCounts.contractCount, icon: 'pi pi-flag' },
            /*{ title: 'Nombre de Factures', count: this.dashboardCounts.invoiceCount, icon: 'pi pi-list' }*/
        ];
      },
      error => {
        console.error('Error loading dashboard counts', error);
      }
    );
  }
}