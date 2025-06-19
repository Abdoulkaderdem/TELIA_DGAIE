import { OnInit } from '@angular/core';
import { Component } from '@angular/core';

@Component({
    selector: 'app-menu',
    templateUrl: './app.menu.component.html',
})
export class AppMenuComponent implements OnInit {
    model: any[] = [];

    ngOnInit() {
        this.model = [
            {
                label: 'ACCUEIL',
                icon: 'pi pi-home',
                items: [
                    {
                        label: 'Vue d\'ensemble',
                        icon: 'pi pi-fw pi-home',
                        routerLink: ['/'],
                    },
                    {
                        label: 'Notifications',
                        icon: 'pi pi-fw pi-bell',
                        routerLink: ['/notifications'],
                    },
                    {
                        label: 'Page de depart',
                        icon: 'pi pi-fw pi-bell',
                        routerLink: ['/starter'],
                    },
                ],
            },
            {
                label: 'GESTION DES BAUX',
                icon: 'pi pi-briefcase',
                items: [
                    {
                        label: 'Demandes',
                        icon: 'pi pi-fw pi-file',
                        items: [
                            { label: 'Soumettre une demande', routerLink: ['/leases/requests/submit'] },
                            { label: 'Suivi des demandes', routerLink: ['/leases/requests/status'] },
                        ],
                    },
                    {
                        label: 'Offres',
                        icon: 'pi pi-fw pi-list',
                        items: [
                            { label: 'Liste des offres', routerLink: ['/leases/offers/list'] },
                            { label: 'Soumettre une offre', routerLink: ['/leases/offers/submit'] },
                        ],
                    },
                    {
                        label: 'Arbitrage',
                        icon: 'pi pi-fw pi-check',
                        routerLink: ['/leases/arbitration'],
                    },
                    {
                        label: 'Immeubles',
                        icon: 'pi pi-fw pi-building',
                        items: [
                            { label: 'Identification des immeubles', routerLink: ['/leases/buildings/identify'] },
                            { label: 'Inspection technique', routerLink: ['/leases/buildings/inspection'] },
                        ],
                    },
                    {
                        label: 'Loyers',
                        icon: 'pi pi-fw pi-dollar',
                        routerLink: ['/leases/rents'],
                    },
                    {
                        label: 'Dossiers',
                        icon: 'pi pi-fw pi-folder',
                        items: [
                            { label: 'Examen par la CNOI', routerLink: ['/leases/files/examination'] },
                            { label: 'Mise en œuvre des décisions', routerLink: ['/leases/files/implementation'] },
                        ],
                    },
                ],
            },
            {
                label: 'PRODUCTION DE STATISTIQUES',
                icon: 'pi pi-chart-bar',
                items: [
                    {
                        label: 'États statistiques',
                        icon: 'pi pi-fw pi-chart-line',
                        routerLink: ['/statistics/reports'],
                    },
                    {
                        label: 'Critères de production',
                        icon: 'pi pi-fw pi-filter',
                        routerLink: ['/statistics/criteria'],
                    },
                    {
                        label: 'Rapports personnalisés',
                        icon: 'pi pi-fw pi-file',
                        routerLink: ['/statistics/custom-reports'],
                    },
                ],
            },
            {
                label: 'PARTAGE D\'INFORMATIONS',
                icon: 'pi pi-share-alt',
                items: [
                    {
                        label: 'SI-N@FOLO',
                        icon: 'pi pi-fw pi-sitemap',
                        routerLink: ['/integration/sinafolo'],
                    },
                    {
                        label: 'SIGCM',
                        icon: 'pi pi-fw pi-sitemap',
                        routerLink: ['/integration/sigcm'],
                    },
                    {
                        label: 'Comptes de Gestion',
                        icon: 'pi pi-fw pi-book',
                        routerLink: ['/integration/accounts'],
                    },
                ],
            },
            {
                label: 'SÉCURITÉ',
                icon: 'pi pi-lock',
                items: [
                    {
                        label: 'Gestion des Accès',
                        icon: 'pi pi-fw pi-key',
                        routerLink: ['/security/access-management'],
                    },
                    {
                        label: 'Signature Électronique',
                        icon: 'pi pi-fw pi-sign-in',
                        routerLink: ['/security/electronic-signature'],
                    },
                    {
                        label: 'Politique de Sécurité',
                        icon: 'pi pi-fw pi-shield',
                        routerLink: ['/security/policy'],
                    },
                ],
            },
            {
                label: 'PARAMÈTRE',
                icon: 'pi pi-spin',
                items: [
                    {
                        label: 'Paramètres',
                        icon: 'pi pi-spin pi-cog',
                        routerLink: ['/settings'],
                    },
                ],
            },
        ];
    }
}
