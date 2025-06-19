import { Component, OnInit } from '@angular/core';
import { TokenService } from 'src/app/services/token/token.service';
import { AuthenticationService } from 'src/app/services/auth/authentication.service';
import { MenuItem } from 'primeng/api';
import { StructureList } from './structure-list/structure-list.component';
import { MinistryList } from './ministry-list/ministry-list.component';
import { CharacteristicsComponent } from './characteristics-usage-list/characteristics.component';

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.css'],
})

export class SettingsComponent {

  constructor() { }

  onTabChange(event: any): void {
    const tabIndex = event.index;
    switch (tabIndex) {
      case 0:
        this.refreshMinistryTab();
        break;
      case 1:
        this.refreshStructureTab();
        break;
      case 2:
        this.refreshTypeUsageTab();
        break;
      case 3:
        this.refreshCharacteristicsTab();
        break;
      case 4:
        this.refreshValidationCommitteeTab();
        break;
    }
  }

  refreshMinistryTab(): void {
    // Logic to refresh ministry tab
  }

  refreshStructureTab(): void {
    // Logic to refresh structure tab
  }

  refreshTypeUsageTab(): void {
    // Logic to refresh type usage tab
  }

  refreshCharacteristicsTab(): void {
    // Logic to refresh characteristics tab
  }

  refreshValidationCommitteeTab(): void {
    // Logic to refresh validation committee tab
  }
}
