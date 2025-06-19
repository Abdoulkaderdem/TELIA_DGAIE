import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { catchError, finalize, tap, of } from 'rxjs';
import { ICredential } from 'src/app/interfaces/credentials';
import { AuthenticationService } from 'src/app/services/auth/authentication.service';
import { TokenService } from 'src/app/services/token/token.service';
import { TokenStatusInterface, TokenResponse } from 'src/app/interfaces/token';

@Component({
  selector: 'app-sign-in',
  templateUrl: './sign-in.component.html',
  styleUrls: ['./sign-in.component.css']
})
export class SignInComponent implements OnInit {
  @Input() nextUrl!: string | undefined;
  showErrorMessage = false;
  passwordVisible = false;
  submitting = false;
  loginForm!: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private authAPI: AuthenticationService,
    private tokenService: TokenService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.initializeForm();
    this.clearTokenIfExists();
  }

  initializeForm(): void {
    this.loginForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      pass: ['', [Validators.required, Validators.minLength(3)]],
    });
  }

  clearTokenIfExists(): void {
    const token = this.tokenService.getToken();
    if (token) {
      this.tokenService.clearToken();
    }
  }

  onSubmit(): void {
    this.submitting = true;
    this.showErrorMessage = false;

    if (this.loginForm.valid) {
      const credentials: ICredential = {
        username: this.loginForm.value.email,
        password: this.loginForm.value.pass,
      };
      this.loginUser(credentials);
    } else {
      this.submitting = false;
    }
  }

  loginUser(credentials: ICredential): void {
    this.authAPI.login(credentials).pipe(
      tap((response: TokenResponse) => this.handleSuccess(response)),
      catchError((error: any) => this.handleError(error)),
      finalize(() => this.submitting = false)
    ).subscribe();
  }

  handleSuccess(response: TokenResponse): void {
    this.showErrorMessage = false;
    this.tokenService.saveToken(response.access_token);
    this.router.navigate(['admin']);
    this.authAPI.getProfile(data => this.authAPI.profile = data.data);
  }

  handleError(error: any): never[] {
    console.error("Échec de la sauvegarde", error);
    this.showErrorMessage = true;
    return [];
  }

  showHidePass(): void {
    this.passwordVisible = !this.passwordVisible;
  }
}
