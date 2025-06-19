import { Component, OnInit } from '@angular/core';
import { RequestService } from 'src/app/services/request.service';
import { Request } from 'src/app/models/request.model';
import { RentalRequest } from 'src/app/interfaces/rental-request';
import { MessageService, ConfirmationService } from 'primeng/api';
import { InvoiceService } from 'src/app/services/invoice.service';

@Component({
  selector: 'app-invoice-list',
  templateUrl: './invoice-list.component.html',
  providers: [MessageService, ConfirmationService]
})
export class InvoiceList implements OnInit {
  invoices: any[] = [];
  selectedInvoice: any;
  invoiceDialog: boolean = false;
  cols: any[] = [];

  constructor(
    private requestService: RequestService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService,
    private invoiceService: InvoiceService
  ) {}

  ngOnInit() {
    this.loadInvoices();
  }

  findIndexById(id: number): number {
    //return this.requests.findIndex(req => req.id === id);
    return 0;
  }

  exporter(){

  }

  loadInvoices() {
    this.invoiceService.getAllInvoices().subscribe(
      data => {
        this.invoices = data;
      },
      error => {
        console.error('Erreur lors du chargement des factures', error);
      }
    );
  }

  viewInvoiceDetails(invoice: any) {
    this.selectedInvoice = invoice;
    this.invoiceDialog = true;
  }

  hideDialog() {
    this.invoiceDialog = false;
  }

  formatDate(date: string): string {
    return new Date(date).toLocaleDateString();
  }

  sortByDueDate() {
    this.invoices.sort((a, b) => new Date(a.dueDate).getTime() - new Date(b.dueDate).getTime());
  }

  printInvoice(invoiceId: number): void {
    this.invoiceService.printInvoice(invoiceId).subscribe(blob => {
      const a = document.createElement('a');
      const objectUrl = URL.createObjectURL(blob);
      a.href = objectUrl;
      const currentDate = new Date().toISOString();
      a.download = `facture_${invoiceId}_${currentDate}.pdf`;
      a.click();
      URL.revokeObjectURL(objectUrl);
    }, error => {
      console.log(error);
      this.messageService.add({
        severity: 'error',
        summary: 'Echec',
        detail: 'Echec d\'impression de la facture',
        life: 3000
      });
    });
  }

  onGlobalFilter(event: Event) {
    const value = (event.target as HTMLInputElement).value;
    //this.requests = this.requests.filter(req => req.description.toLowerCase().includes(value.toLowerCase()));
  }

  formatDate2(dateString: string): string {
    const date = new Date(dateString);
    return date.toLocaleDateString() + ' ' + date.toLocaleTimeString();
  }
}