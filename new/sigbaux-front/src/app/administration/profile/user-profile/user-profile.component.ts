import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { EUserType } from 'src/app/interfaces/enum-userType';
import { UserProfileResponse } from 'src/app/interfaces/user_profile_response';
import { AuthenticationService } from 'src/app/services/auth/authentication.service';
import { TokenService } from 'src/app/services/token/token.service';
import { UsersService } from 'src/app/services/users/users.service';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent implements OnInit {

  userConnected!: UserProfileResponse['data'];

  isEditMode: Boolean = false;
  //userConnected!: UserInterface
  hidden = true
  constructor(
    private authService: AuthenticationService,
    private tokenService: TokenService,
    private activeRote: ActivatedRoute,
    private userService: UsersService
  ) { }

  ngOnInit(): void {
    this.tokenService.userAlwaysConnected();
    this.loadIdentity();
  }


  editDetails() {
    this.isEditMode = true;
  }

  loadIdentity() {
    const id = this.activeRote.snapshot.paramMap.get('userId')
    if (id) {
      this.userService.getUser(Number(id)).subscribe(user => {
        console.log(user)
        //this.userConnected = user
      })
    } else {
      this.authService.getAuthenticatedUser().subscribe(
        response => {
          if (response.status == 200) {
            this.hidden = false
            //this.userConnected = response.data;
          }
        },
        error => {
          console.log(error);
        }
      );
    }
  }


}
