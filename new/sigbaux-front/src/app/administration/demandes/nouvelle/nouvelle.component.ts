import { Component, OnInit, Input } from '@angular/core';
import { RequestService } from 'src/app/services/request.service';
import { Request } from 'src/app/models/request.model';
import { RentalRequest } from 'src/app/interfaces/rental-request';
import { MessageService, ConfirmationService } from 'primeng/api';
import { Ministry } from 'src/app/interfaces/ministry';

@Component({
  selector: 'app-nouvelle',
  templateUrl: './nouvelle.component.html',
  providers: [MessageService, ConfirmationService]
})
export class NouvelleComponent {
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
    private confirmationService: ConfirmationService
  ) {}

  ngOnInit() {

    this.cols = [
      { field: 'id', header: 'No' },
      { field: 'dateRequest', header: 'Date' },
      { field: 'ministere', header: 'Ministere' },
      { field: 'structure', header: 'Structure' },
      { field: 'agentsNumber', header: 'Agents' },
      { field: 'managersNumber', header: 'Managers' },
      { field: 'leasePortfolioMinistry', header: 'Ministry' }
    ];
  }

  openNew() {
    this.request = {} as Request;
    this.submitted = false;
    this.showDialog = true;
  }

  editRequest(request: RentalRequest) {
    //this.request = { ...request };
    this.rentalRequest= { ...request };
    this.showDialog = true;
  }

  valideRequestConfirmation(request: Request) {
    this.validateRequestDialog = true;
    this.request = { ...request };
  }

  confirmDelete() {
    this.validateRequestDialog = false;
    this.requests = this.requests.filter(val => val.id !== this.request.id);
    this.messageService.add({ severity: 'success', summary: 'Successful', detail: 'Request Deleted', life: 3000 });
    this.request = {} as Request;
  }

  validateRequest() {
    this.requestService.changeRequestStatus('validate',this.request.id).subscribe(() => {
      this.requestService.getRequestsByStatus("NEW").subscribe(data => (this.requests = data));
      this.messageService.add({ severity: 'success', summary: 'Successful', detail: 'demande validée avec succes !', life: 3000 });
      this.validateRequestDialog = false;
      this.request = {} as Request;
    });
    
  }

  hideDialog() {
    this.showDialog = false;
    this.submitted = false;
  }

  saveRequest() {
    this.submitted = true;

    if (this.request.description?.trim()) {
      if (this.request.id) {
        this.requestService.updateRequest(this.request).subscribe(() => {
          this.requests[this.findIndexById(this.request.id)] = this.request;
          this.messageService.add({ severity: 'success', summary: 'Successful', detail: 'Request Updated', life: 3000 });
        });
      } else {
        this.requestService.createRequest(this.request).subscribe(data => {
          this.requests.push(data);
          this.messageService.add({ severity: 'success', summary: 'Successful', detail: 'Request Created', life: 3000 });
        });
      }

      this.requests = [...this.requests];
      this.showDialog = false;
      this.request = {} as Request;
    }
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
