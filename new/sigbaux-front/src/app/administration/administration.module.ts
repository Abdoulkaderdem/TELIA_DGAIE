import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AdministrationRoutingModule } from './administration-routing.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ActionResponse } from './action-response/response.component';
import { ResetInput } from './reset-input/reset-input.component';
import { NgxPaginationModule } from 'ngx-pagination';
import { GridComponent } from './composants/grid/grid.component';
import { CardComponent } from './composants/card/card.component';

@NgModule({
  declarations: [
    ResetInput,
    ActionResponse,
  ],
  imports: [
    CommonModule,
    AdministrationRoutingModule,
    ReactiveFormsModule,
    GridComponent,
    CardComponent,
    NgxPaginationModule,
  ],
  exports: [
    ResetInput,
    ActionResponse
  ]
})
export class AdministrationModule { }
