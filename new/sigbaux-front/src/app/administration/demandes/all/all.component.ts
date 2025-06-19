import { Component, OnInit, Input } from '@angular/core';
import { RequestService } from 'src/app/services/request.service';
import { Request } from 'src/app/models/request.model';
import { RentalRequest } from 'src/app/interfaces/rental-request';
import { MessageService, ConfirmationService } from 'primeng/api';
import { PrintService } from 'src/app/services/print.service';
import { MinistryService } from 'src/app/services/ministry.service';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observer, catchError, finalize, tap ,throwError } from 'rxjs';
import { Ministry } from 'src/app/interfaces/ministry';
import { DataListResponse, DataResponse } from 'src/app/interfaces/request-response';

@Component({
  selector: 'app-all',
  templateUrl: './all.component.html',
  providers: [MessageService]
})
export class AllComponent {
  @Input() requests: Request[] = [];
  @Input() ministeres: Ministry[] = [];
  request: Request = {} as Request;
  rentalRequest: RentalRequest = {} as RentalRequest;
  selectedRequests: Request[] = [];
  showDialog: boolean = false;
  validateRequestDialog: boolean = false;
  submitted: boolean = false;
  
  cols: any[] = [];

  constructor(
    private requestService: RequestService,
    private messageService: MessageService,
    private printService: PrintService
  ) {}

  ngOnInit() {
    
    this.cols = [
      { field: 'id', header: 'No' },
      { field: 'dateRequest', header: 'Date' },
      { field: 'ministere', header: 'Ministere' },
      { field: 'structure', header: 'Structure' },
      { field: 'status', header: 'Status' }
    ];
  }

  openNew() {
    this.request = {} as Request;
    this.submitted = false;
    this.showDialog = true;
  }

  showRequest(request: RentalRequest) {
    this.rentalRequest= { ...request };
    this.showDialog = true;
  }

  printContract(idRequest: number) {
    this.printService.printRequest(idRequest).subscribe(blob => {
      const a = document.createElement('a');
      const objectUrl = URL.createObjectURL(blob);
      a.href = objectUrl;
      const currentDate = new Date().toISOString();
      a.download = `demande_${idRequest}_${currentDate}.pdf`;
      a.click();
      URL.revokeObjectURL(objectUrl);
      this.messageService.add({ severity: 'success', summary: 'Successful', detail: 'Demande téléchargé !', life: 3000 });
    }, error => {
      console.log(error);
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Echec de téléchargement de la demande', life: 3000 });
    });
  }

  hideDialog() {
    this.showDialog = false;
    this.submitted = false;
  }

  findIndexById(id: number): number {
    return this.requests.findIndex(req => req.id === id);
  }

  onGlobalFilter(event: Event) {
    const value = (event.target as HTMLInputElement).value;
    this.requests = this.requests.filter(req => req.description.toLowerCase().includes(value.toLowerCase()));
  }

  formatDate(dateString: string): string {
    const date = new Date(dateString);
    return date.toLocaleDateString() + ' ' + date.toLocaleTimeString();
  }

  getMinistryName(id: number) {
    const ministry = this.ministeres.find(m => m.id === id);
    return ministry ? ministry!.name : " - ";
  }
}
