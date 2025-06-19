import { Component, OnInit } from '@angular/core';
import { TokenService } from 'src/app/services/token/token.service';
import { AuthenticationService } from 'src/app/services/auth/authentication.service';
import { MenuItem } from 'primeng/api';
@Component({
  selector: 'app-settings',
  templateUrl: './meo.component.html',
  styleUrls: ['./meo.component.css'],
})

export class MeoComponent implements OnInit {

  constructor() { }

  ngOnInit() {}
}