import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable, Observer, catchError, of, switchMap } from 'rxjs';
import { RoleType } from 'src/app/interfaces/enum-roles';
import { UserProfileResponse, UserProfileData } from 'src/app/interfaces/user_profile_response';
import { AuthenticationService } from 'src/app/services/auth/authentication.service';
import { TokenService } from 'src/app/services/token/token.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  constructor(
    private router: Router,
    private tokenService: TokenService,
    private authService: AuthenticationService
  ) { }

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    
    return this.authService.getAuthenticatedUser().pipe(
      switchMap((response) => {
        const user: UserProfileData | undefined  = response?.data;
        if (user == undefined /*|| user.role == RoleType.BAILLEUR*/) {
          return of(false);
        }

        /*const path = route.routeConfig?.path;
        switch (user.data.role) {
          case RoleType.ADMIN:
            return of(true);
          default:
            return of(false);
        }*/

        return of(true);
      }),
      catchError((error) => {
        //console.log(error);
        return of(false);
      })
    );
  }

}