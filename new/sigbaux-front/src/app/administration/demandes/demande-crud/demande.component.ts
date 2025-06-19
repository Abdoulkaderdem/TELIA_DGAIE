// request-list.component.ts
import { Component, OnInit } from '@angular/core';
import { RequestService } from 'src/app/services/request.service';
import { Request } from 'src/app/models/request.model';
import { MessageService, ConfirmationService } from 'primeng/api';

@Component({
  selector: 'app-request-list',
  templateUrl: './demande.component.html',
  providers: [MessageService, ConfirmationService]
})
export class RentalRequestsComponent implements OnInit {
  requests: Request[] = [];
  request: Request = {} as Request;
  selectedRequests: Request[] = [];
  requestDialog: boolean = false;
  deleteRequestDialog: boolean = false;
  deleteRequestsDialog: boolean = false;
  submitted: boolean = false;
  cols: any[] = [];

  constructor(
    private requestService: RequestService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService
  ) {}

  ngOnInit() {
    this.requestService.getRequests().subscribe(data => (this.requests = data));

    this.cols = [
      { field: 'dateRequest', header: 'Date' },
      { field: 'description', header: 'Description' },
      { field: 'legalStatus', header: 'Legal Status' },
      { field: 'agentsNumber', header: 'Agents' },
      { field: 'managersNumber', header: 'Managers' },
      { field: 'leasePortfolioMinistry', header: 'Ministry' }
    ];
  }

  openNew() {
    this.request = {} as Request;
    this.submitted = false;
    this.requestDialog = true;
  }

  deleteSelectedRequests() {
    this.deleteRequestsDialog = true;
  }

  editRequest(request: Request) {
    this.request = { ...request };
    this.requestDialog = true;
  }

  deleteRequest(request: Request) {
    this.deleteRequestDialog = true;
    this.request = { ...request };
  }

  confirmDeleteSelected() {
    this.deleteRequestsDialog = false;
    this.requests = this.requests.filter(val => !this.selectedRequests.includes(val));
    this.messageService.add({ severity: 'success', summary: 'Successful', detail: 'Requests Deleted', life: 3000 });
    this.selectedRequests = [];
  }

  confirmDelete() {
    this.deleteRequestDialog = false;
    this.requests = this.requests.filter(val => val.id !== this.request.id);
    this.messageService.add({ severity: 'success', summary: 'Successful', detail: 'Request Deleted', life: 3000 });
    this.request = {} as Request;
  }

  hideDialog() {
    this.requestDialog = false;
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
      this.requestDialog = false;
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
}
