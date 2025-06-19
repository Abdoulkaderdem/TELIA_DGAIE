import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { EUserType } from 'src/app/interfaces/enum-userType';
import { AuthenticationService } from 'src/app/services/auth/authentication.service';
import { TokenService } from 'src/app/services/token/token.service';
import { UsersService } from 'src/app/services/users/users.service';
import { UserProfileResponse,UserInterface } from 'src/app/interfaces/user_profile_response';

@Component({
  selector: 'app-user-edit-profile',
  templateUrl: './user-edit-profile.component.html',
  styleUrls: ['./user-edit-profile.component.css']
})
export class UserEditProfileComponent implements OnInit{

  userConnected: UserInterface = {
    id: null,
    firstName: null,
    lastName: null,
    email: null,
    password: null,
    matricule: null,
    typeUser: null,
    role: null
  }

  

  idUserEdit!: number;

  constructor(
    private activated: ActivatedRoute,
    private authService: AuthenticationService,
    private tokenService: TokenService,
    private router: Router,
    private userService: UsersService
  ){}


  ngOnInit(): void {
    this.tokenService.userAlwaysConnected();
    if (this.activated.snapshot.paramMap.get('uid')) {
      let uid = Number(this.activated.snapshot.paramMap.get('uid'));
      this.idUserEdit =uid;

    this.loadIdentity();
    }
  }

  loadIdentity(){
    this.authService.getAuthenticatedUser().subscribe(
      response=>{
        //console.log("Les identifiants:.........", response);
        this.userConnected = response.data;
      },
      error=> {
        console.log(error);
      }
    );
    
  }

  
  onSubmit(){
    this.userService.updateUser(this.userConnected).subscribe(
      response=> {
        //console.log(response);
        this.router.navigate(['admin/profile']);

      },
      error=>{
        console.log(error);
      }
    );

  }


}
