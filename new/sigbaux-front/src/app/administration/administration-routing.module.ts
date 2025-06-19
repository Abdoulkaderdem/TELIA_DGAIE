import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from '../_helpers/guards/auth.guard';
import { AppLayoutComponent } from '../layout/app.layout.component';

const routes: Routes = [
  {
    path: '',
    component: AppLayoutComponent,
    children: [
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
      {
        path: 'dashboard',
        loadChildren: () => import('./dashboard/dashboard.module').then((m) => m.DashboardModule),
        canActivate: [AuthGuard]
      },
      {
        path: 'offres',
        loadChildren: () => import('./offres/offres.module').then((m) => m.OffresModule),
        canActivate: [AuthGuard]
      },
      {
        path: 'demandes',
        loadChildren: () => import('./demandes/demande.module').then((m) => m.DemandeModule),
        canActivate: [AuthGuard]
      },
      {
        path: 'users',
        loadChildren: () => import('./users/user.module').then((m) => m.UserModule),
        canActivate: [AuthGuard]
      },
      {
        path: 'settings', 
        loadChildren: () => import('./settings/settings.module').then(m => m.SettingsModule), 
        canActivate: [AuthGuard]
      },
      {
        path: 'immeubles', 
        loadChildren: () => import('./conformite/immeubles/immeuble.module').then(m => m.ImmeubleModule), 
        canActivate: [AuthGuard]
      },
      {
        path: 'inspections', 
        loadChildren: () => import('./conformite/inspections/inspections.module').then(m => m.InspectionsModule), 
        canActivate: [AuthGuard]
      },
      {
        path: 'contrats', 
        loadChildren: () => import('./contrats/contrat.module').then(m => m.ContratModule), 
        canActivate: [AuthGuard]
      },
      {
        path: 'examen_cnoi', 
        loadChildren: () => import('./conformite/examen_cnoi/cnoi.module').then(m => m.CnoiModule), 
        canActivate: [AuthGuard]
      },
      {
        path: 'meo', 
        loadChildren: () => import('./conformite/mise-en-oeuvre/meo.module').then(m => m.MeoModule), 
        canActivate: [AuthGuard]
      },
      {
        path: 'loyers', 
        loadChildren: () => import('./loyers/loyer.module').then(m => m.LoyerModule), 
        canActivate: [AuthGuard]
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdministrationRoutingModule { }