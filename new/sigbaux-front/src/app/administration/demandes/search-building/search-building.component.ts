import { Component } from '@angular/core';
import { MessageService,ConfirmationService } from 'primeng/api';
import { RequestService } from 'src/app/services/request.service';
import { Router } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { Building } from 'src/app/interfaces/building';
import { AttachBuildingsToRequestDto } from 'src/app/interfaces/attach-buildings-request';

@Component({
  selector: 'app-search-building',
  templateUrl: './search-building.component.html',
  styleUrl: './search-building.component.css',
  providers: [MessageService, ConfirmationService]
})
export class SearchBuildingComponent {
  requestId: number | null = null;
  buildings: Building[] = [];
  selectedBuildings: Building[] = [];
  cols: any[] = [];

  constructor(
    private requestService: RequestService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService,
    private route: ActivatedRoute,
    private router: Router,
  ) {}

  ngOnInit() {
    this.requestId = +this.route.snapshot.paramMap.get('id')!;
    this.requestService.searchBuildings(this.requestId).subscribe(
      data => (this.buildings = data.data as Building[])
    );

    this.cols = [
      { field: 'typeBuilding', header: 'Type Building' },
      { field: 'typePropertyTitle', header: 'Property Title' },
      { field: 'buildingValue', header: 'Value' },
      { field: 'region', header: 'Region' },
      { field: 'province', header: 'Province' },
      { field: 'commune', header: 'Commune' },
      { field: 'city', header: 'City' },
      { field: 'district', header: 'District' },
      { field: 'sector', header: 'Sector' },
      { field: 'section', header: 'Section' },
      { field: 'ilot', header: 'Ilot' },
      { field: 'plot', header: 'Plot' },
      { field: 'street', header: 'Street' },
      { field: 'doornumber', header: 'Door Number' },
      { field: 'geolocation', header: 'Geolocation' },
      { field: 'rentPrice', header: 'Rent Price' },
      { field: 'code', header: 'Code' }
    ];
  }

  ratacher(){
    const listID: number[] = this.selectedBuildings.map(building => building.id!);
    const data: AttachBuildingsToRequestDto = {
      rentalRequestId: this.requestId!,
      buildingIds: listID
    };

    console.log(data);
    this.requestService.attachBuildings(data).subscribe(
      (reponse)=> {
        console.log(reponse);
        this.messageService.add({severity:'success', summary: 'Succès', detail: 'Batiment ratchée avec succès !', life: 3000});
        if(this.requestId){
          this.requestService.searchBuildings(this.requestId).subscribe(
            data => (this.buildings = data.data as Building[])
          );
        }
      }
    );
  }

  onGlobalFilter(event: Event) {
    const value = (event.target as HTMLInputElement).value;
    this.buildings = this.buildings.filter(req => req.code!.toLowerCase().includes(value.toLowerCase()));
  }
}
