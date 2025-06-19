import { RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';
import { UserList } from './user-list/user-list.component';

const routes: Routes = [
  //{ path: '', redirectTo: 'user-list', pathMatch: 'full' },
  { path: 'user-list', component: UserList },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})

export class UserRoutingModule {}
