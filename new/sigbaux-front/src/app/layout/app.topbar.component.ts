import { Component, ElementRef, ViewChild } from '@angular/core';
import { AppConfig, LayoutService } from './service/app.layout.service';
import { AuthenticationService } from 'src/app/services/auth/authentication.service';
import { Router } from '@angular/router';
import { UserProfileResponse } from '../interfaces/user_profile_response';
import { tap } from 'rxjs';

@Component({
    selector: 'app-topbar',
    templateUrl: './app.topbar.component.html',
  })
  export class AppTopbarComponent {
    @ViewChild('menuButton') menuButton!: ElementRef;
    @ViewChild('mobileMenuButton') mobileMenuButton!: ElementRef;
  
    config!: AppConfig;
    user!: UserProfileResponse['data'];
    subscription: any;
    loading = false;
  
    constructor(
      private router: Router,
      public layoutService: LayoutService, 
      public el: ElementRef,
      private authAPI: AuthenticationService,
    ) {
      this.subscription = this.layoutService.configUpdate$.subscribe(
        (config) => {
          this.config = config;
        }
      );
  
      this.loadUserProfile();
    }
  
    loadUserProfile() {
      this.authAPI.getAuthenticatedUser().pipe(
        tap((response) => {
          if (response!.status === 200) {
            this.user = response!.data;
          } else {
            console.error('Erreur lors de la récupération du profil:', response!.message);
          }
        })
      ).subscribe();
    }
  
    onMenuButtonClick() {
      this.layoutService.onMenuToggle();
    }
  
    load() {
      this.loading = true;
      setTimeout(() => (this.loading = false), 1000);
    }
  
    logout() {
      this.authAPI.logout();
      this.router.navigate(['']);
    }
  }  