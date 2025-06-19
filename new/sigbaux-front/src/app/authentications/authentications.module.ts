import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AuthenticationsRoutingModule } from './authentications-routing.module';
import { SignInComponent } from './sign-in/sign-in.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
//import { MatCardModule } from '@angular/material/card';
import { AdministrationModule } from '../administration/administration.module';



import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { CheckboxModule } from 'primeng/checkbox';
import { RippleModule } from 'primeng/ripple';
import { AppConfigModule } from 'src/app/layout/config/app.config.module';


@NgModule({
  declarations: [
    SignInComponent
  ],
  imports: [
    CommonModule,
    AuthenticationsRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    //MatCardModule,
    AdministrationModule,
    ButtonModule,
    InputTextModule,
    CheckboxModule,
    RippleModule,
    AppConfigModule
  ]
})
export class AuthenticationsModule { }
