import { OnInit } from '@angular/core';
import { Component } from '@angular/core';
import { icon } from 'leaflet';

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
                icon: 'pi pi-fw pi-home',
                routerLink: ['/admin']
            },
            {
                label: 'GESTION OFFRES',
                icon: 'pi pi-fw pi-building',
                items: [
                    { label: 'Registre des offres',routerLink: ['/offres/list-offres'] },
                    { label: 'Enregistrer une offre', routerLink: ['/offres/new']}
                ]
            },
            {
                label: 'GESTION DEMANDES',
                icon: 'pi pi-fw pi-book',
                items: [
                    { label: 'Registre des demandes', routerLink: ['/demandes/list-demandes'] },
                    { label: 'Soumettre une demande', routerLink: ['/demandes/form-demande'] },
                ]
            },
            {
                label: 'CONFORMITE',
                icon: 'pi pi-fw pi-shield',
                items: [
                    {
                        label: 'Immeubles',
                        icon: 'pi pi-fw pi-building',
                        items: [
                            { label: 'Identification des immeubles', routerLink: ['/immeubles'] },
                            { label: 'Inspection technique', routerLink: ['/inspections'] },
                        ],
                    },
                    {
                        label: 'Dossiers',
                        icon: 'pi pi-fw pi-folder',
                        items: [
                            { label: 'Examen par la CNOI', routerLink: ['/examen_cnoi'] },
                            { label: 'Mise en œuvre des décisions', routerLink: ['/meo'] },
                        ]
                    }
                ]
            },
            {
                label: 'GESTION CONTRATS',
                icon: 'pi pi-fw pi-file-edit',
                items: [
                    { label: 'Registre des contrats', icon: 'pi pi-fw pi-file-edit', routerLink: ['/contrats'] },
                    {
                        label: 'Formalisation',
                        icon: 'pi pi-fw pi-file',
                        items: [
                            { label: 'Révision', routerLink: ['/contrats/revision-contract-form'] },
                            { label: 'PV de remise de clées', routerLink: ['/contrats/pv-remise-clee-form'] },
                            { label: 'Résiliation', routerLink: ['/contrats/resiliation-contract-form'] },
                        ]
                    },
                    {
                        label: 'Indemnisation',
                        icon: 'pi pi-fw pi-dollar',
                        items: [
                            { label: 'Traitement IRE', routerLink: ['/contrats/indemnisation-contract'] },
                            { label: 'Historique', routerLink: ['/leases/contract/compensation/methods'] }
                        ]
                    }
                ]
            },
            /*{
                label: 'GESTION CONTRATS',
                icon: 'pi pi-fw pi-file-edit',
                items: [
                    {
                        label: 'Suivi',
                        icon: 'pi pi-fw pi-cog',
                        items: [
                            { label: 'Contrats', routerLink: ['/contrats'] },
                            { label: 'Révision', routerLink: ['/contrats/revision-contract-form'] },
                            { label: 'PV de remise de clées', routerLink: ['/contrats/revision-contract-form'] },
                            { label: 'Contrôle d’occupation', routerLink: ['/leases/contract/followup/occupancy'] },
                            { label: 'Résiliation', routerLink: ['/contrats/resiliation-contract-form'] },
                        ]
                    },
                    {
                        label: 'Formalisation',
                        icon: 'pi pi-fw pi-file',
                        items: [
                            { label: 'Avenant', routerLink: ['/leases/rent/formalization/amendment'] },
                            { label: 'Vérification', routerLink: ['/leases/rent/formalization/verification'] },
                            { label: 'Approbation', routerLink: ['/leases/rent/formalization/approval'] },
                        ]
                    },
                    {
                        label: 'Indemnisation',
                        icon: 'pi pi-fw pi-dollar',
                        items: [
                            { label: 'Traitement IRE', routerLink: ['/contrats/indemnisation-contract'] },
                            { label: 'Modalités d’indemnisation', routerLink: ['/leases/contract/compensation/methods'] },
                            { label: 'Roc pour Paiement', routerLink: ['/leases/contract/compensation/payment'] },
                        ]
                    }
                ]
            },*/
            {
                label: 'GESTION FACTURES',
                icon: 'pi pi-fw pi-dollar',
                items: [
                    { label: 'Enregistrer une facture', routerLink: ['/loyers/invoice-form'] },
                    { label: 'Registres des Factures', routerLink: ['/loyers/invoice-list'] }
                ]
            },
            /*
            {
                label: 'GESTION LOYERS',
                icon: 'pi pi-fw pi-dollar',
                items: [
                    {
                        label: 'Factures',
                        icon: 'pi pi-fw pi-dollar', 
                        items: [
                            { label: 'Enregistrer une facture', routerLink: ['/loyers/invoice-form'] },
                            { label: 'Registres des Factures', routerLink: ['/loyers/invoice-list'] },
                            { label: 'Examen Révision loyer', routerLink: ['/leases/rent/evaluation/examination'] },
                        ]
                    },
                    {
                        label: 'Évaluation',
                        icon: 'pi pi-fw pi-check',
                        items: [
                            { label: 'Condition de Révision', routerLink: ['/leases/rent/evaluation/conditions'] },
                            { label: 'Traitement demande de révision', routerLink: ['/leases/rent/evaluation/requests'] },
                            { label: 'Examen Révision loyer', routerLink: ['/leases/rent/evaluation/examination'] },
                        ]
                    },
                    {
                        label: 'Formalisation',
                        icon: 'pi pi-fw pi-file',
                        items: [
                            { label: 'Avenant', routerLink: ['/leases/rent/formalization/amendment'] },
                            { label: 'Vérification', routerLink: ['/leases/rent/formalization/verification'] },
                            { label: 'Approbation', routerLink: ['/leases/rent/formalization/approval'] },
                        ]
                    }
                ]
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
                    { label: 'IFU', icon: 'pi pi-fw pi-sitemap', routerLink: ['/integration/ifu'] },
                    / *{ label: 'Comptes de Gestion', icon: 'pi pi-fw pi-book', routerLink: ['/integration/accounts'] },* /
                ],
            },
            {
                label: 'SIG-UTIL',
                icon: 'pi pi-lock',
                items: [
                    { label: 'Gestion des Accès', icon: 'pi pi-fw pi-key', routerLink: ['/users/user-list'] },
                    { label: 'Signature Électronique', icon: 'pi pi-fw pi-sign-in', routerLink: ['/security/electronic-signature'] },
                    { label: 'Politique de Sécurité', icon: 'pi pi-fw pi-shield', routerLink: ['/security/policy'] },
                ]
            },*/
            {
                label: 'PARAMÈTRES',
                icon: 'pi pi-spin pi-cog',
                items: [
                    /*
                        { label: 'Ministère et institutions', icon: 'pi pi-fw pi-users', routerLink: ['/ministry'] },
                        { label: 'Structures', icon: 'pi pi-fw pi-users', routerLink: ['/ministry/structures'] },
                        { label: 'Cartographie', icon: 'pi pi-fw pi-map', routerLink: ['/cartographie'] },
                     */
                    { label: 'Données de base', icon: 'pi pi-spin pi-cog', routerLink: ['/settings'] },
                    { label: 'Cartographie', icon: 'pi pi-fw pi-map', routerLink: ['/settings/mapping'] },
                ]
            }
        ];

    }
}