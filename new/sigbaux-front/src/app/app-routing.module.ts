import { NgModule } from '@angular/core';
import { ExtraOptions, RouterModule, Routes } from '@angular/router';
import { AppLayoutComponent } from './layout/app.layout.component';
import { AuthGuard } from './_helpers/guards/auth.guard';

const routerOptions: ExtraOptions = {
    anchorScrolling: 'enabled',
    scrollPositionRestoration: 'enabled',
};

const routes: Routes = [
    {
        path: '',
        loadChildren: () => import('./authentications/authentications.module')
          .then(m => m.AuthenticationsModule)
    },
    {
        path: 'admin',
        loadChildren: () => import('./administration/administration.module')
            .then(m => m.AdministrationModule), canActivate:[AuthGuard]
    },
    {
        path: 'notfound',
        component: AppLayoutComponent,
        loadChildren: () => import('./notfound/notfound.module')
            .then(m => m.NotfoundModule)
    },
    { path: '**', redirectTo: '/notfound' },
];

@NgModule({
    imports: [RouterModule.forRoot(routes, routerOptions)],
    exports: [RouterModule],
})
export class AppRoutingModule {}
