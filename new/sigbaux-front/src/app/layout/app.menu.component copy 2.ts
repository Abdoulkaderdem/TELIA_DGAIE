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
                        routerLink: ['/admin'],
                    }
                ],
            },
            {
                label: 'GESTION DES OFFRES',
                icon: '',
                items: [
                    {
                        label: 'Registre des offres',
                        icon: 'pi pi-fw pi-list',
                        routerLink: ['/admin/liste_offre']
                    },
                    {
                        label: 'Enregistrer une offre',
                        icon: 'pi pi-fw pi-list',
                        routerLink: ['/admin/ajout_offre']
                    }
                ],
            },
            {
                label: 'GESTION DES DEMANDES',
                icon: '',
                items: [
                    {
                        label: 'Offres',
                        icon: 'pi pi-fw pi-list',
                        items: [
                            { label: 'Liste des offres', routerLink: ['/liste_offre'] },
                            { label: 'Soumettre une offre', routerLink: ['/soumettre_offre'] },
                        ],
                    },
                    {
                        label: 'Demandes',
                        icon: 'pi pi-fw pi-file',
                        items: [
                            { label: 'Soumettre une demande', routerLink: ['/leases/requests/submit'] },
                            { label: 'Suivi des demandes', routerLink: ['/leases/requests/status'] },
                        ],
                    },
                ],
            },
            {
                label: 'SIG-CONFORMITE',
                icon: 'pi pi-spin',
                items: [
                    {
                        label: 'Immeubles',
                        icon: 'pi pi-fw pi-building',
                        items: [
                            { label: 'Identification des immeubles', routerLink: ['/leases/buildings/identify'] },
                            { label: 'Inspection technique', routerLink: ['/leases/buildings/inspection'] },
                        ],
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
                label: 'SIG-CONTRAT',
                icon: 'pi pi-spin',
                items: [
                    {
                        label: 'Suivi',
                        icon: 'pi pi-fw pi-cog',
                        items: [
                            { label: 'Traitement et contrôle', routerLink: ['/leases/contract/followup/treatment'] },
                            { label: 'Contrôle d’occupation', routerLink: ['/leases/contract/followup/occupancy'] },
                            { label: 'Résiliation', routerLink: ['/leases/contract/followup/termination'] },
                        ],
                    },
                    {
                        label: 'Indemnisation',
                        icon: 'pi pi-fw pi-dollar',
                        items: [
                            { label: 'Traitement IRE', routerLink: ['/leases/contract/compensation/ire'] },
                            { label: 'Modalités d’indemnisation', routerLink: ['/leases/contract/compensation/methods'] },
                            { label: 'Roc pour Paiement', routerLink: ['/leases/contract/compensation/payment'] },
                        ],
                    },
                ],
            },
            {
                label: 'SIG-LOYER',
                icon: 'pi pi-spin',
                items: [
                    {
                        label: 'Évaluation',
                        icon: 'pi pi-fw pi-check',
                        items: [
                            { label: 'Condition de Révision', routerLink: ['/leases/rent/evaluation/conditions'] },
                            { label: 'Traitement demande de révision', routerLink: ['/leases/rent/evaluation/requests'] },
                            { label: 'Examen Révision loyer', routerLink: ['/leases/rent/evaluation/examination'] },
                        ],
                    },
                    {
                        label: 'Formalisation',
                        icon: 'pi pi-fw pi-file',
                        items: [
                            { label: 'Avenant', routerLink: ['/leases/rent/formalization/amendment'] },
                            { label: 'Vérification', routerLink: ['/leases/rent/formalization/verification'] },
                            { label: 'Approbation', routerLink: ['/leases/rent/formalization/approval'] },
                        ],
                    },
                ],
            },
            {
                label: 'GESTION DES BAUX',
                icon: 'pi pi-briefcase',
                items: [
                    { label: 'Arbitrage', icon: 'pi pi-fw pi-check', routerLink: ['/leases/arbitration'] },
                    { label: 'Loyers', icon: 'pi pi-fw pi-dollar', routerLink: ['/leases/rents'] },
                ],
            },
            {
                label: 'PRODUCTION DE STATISTIQUES',
                icon: 'pi pi-chart-bar',
                items: [
                    { label: 'États statistiques', icon: 'pi pi-fw pi-chart-line', routerLink: ['/statistics/reports'] },
                    { label: 'Critères de production', icon: 'pi pi-fw pi-filter', routerLink: ['/statistics/criteria'] },
                    { label: 'Rapports personnalisés', icon: 'pi pi-fw pi-file', routerLink: ['/statistics/custom-reports'] },
                ],
            },
            {
                label: 'PARTAGE D\'INFORMATIONS',
                icon: 'pi pi-share-alt',
                items: [
                    { label: 'SI-N@FOLO', icon: 'pi pi-fw pi-sitemap', routerLink: ['/integration/sinafolo'] },
                    { label: 'SIGCM', icon: 'pi pi-fw pi-sitemap', routerLink: ['/integration/sigcm'] },
                    { label: 'Comptes de Gestion', icon: 'pi pi-fw pi-book', routerLink: ['/integration/accounts'] },
                ],
            },
            {
                label: 'SIG-UTIL',
                icon: 'pi pi-lock',
                items: [
                    { label: 'Gestion des Accès', icon: 'pi pi-fw pi-key', routerLink: ['/security/access-management'] },
                    { label: 'Signature Électronique', icon: 'pi pi-fw pi-sign-in', routerLink: ['/security/electronic-signature'] },
                    { label: 'Politique de Sécurité', icon: 'pi pi-fw pi-shield', routerLink: ['/security/policy'] },
                ],
            },
            {
                label: 'PARAMÈTRE',
                icon: 'pi pi-spin',
                items: [
                    { label: 'Team', icon: 'pi pi-fw pi-users', routerLink: ['/team'] },
                    { label: 'Ministère et institutions', icon: 'pi pi-fw pi-users', routerLink: ['/ministry'] },
                    { label: 'Cartographie', icon: 'pi pi-fw pi-map', routerLink: ['/cartographie'] },
                    { label: 'Paramètres', icon: 'pi pi-spin pi-cog', routerLink: ['/settings'] },
                ],
            },
        ];

    }
}
