import { Component, OnInit } from '@angular/core';
import { Ministry } from 'src/app/interfaces/ministry';
import { DataListResponse, DataResponse } from 'src/app/interfaces/request-response';
import { MinistryService } from 'src/app/services/ministry.service';
import { ConfirmationService, MessageService } from 'primeng/api';
import { Table } from 'primeng/table';
import { LayoutService } from 'src/app/layout/service/app.layout.service';
import { Observer, catchError, finalize, tap ,throwError } from 'rxjs';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { LandLord } from 'src/app/interfaces/landlord';
import { LandlordService } from 'src/app/services/landlord.service';

@Component({
    selector: 'app-ministry',
    templateUrl: './ministry-list.component.html',
    providers: [MessageService, ConfirmationService]
})
export class MinistryList implements OnInit {

    itemDialog: boolean = false;

    deleteItemDialog: boolean = false;

    deleteItemsDialog: boolean = false;

    items: Ministry[] = [];

    item: Ministry = {};

    selectedItems: Ministry[] = [];

    submitted: boolean = false;

    cols: any[] = [];

    statuses: any[] = [];

    rowsPerPageOptions = [5, 10, 20];

    landlords: LandLord[] = [];

    constructor(
        private ministryService: MinistryService, 
        private messageService: MessageService,
        private landlordService: LandlordService
    ) { }


    
    ngOnInit() {
        this.fetchMinistries();
        this.landlordService.getAllLandLords()
        .pipe(
            catchError((error: HttpErrorResponse) => {
              console.error('Error:', error);
              if (error.status === 302) {
                console.warn('Redirection detected, handling specific logic for status 302');
                if (error.error && Array.isArray(error.error)) {
                  this.landlords = error.error as LandLord[];
                }
              } else {
                console.error(`Unhandled error status: ${error.status}`);
              }
              return throwError(() => error);
            })
          );
    }

    fetchMinistries(): void {
        const observer: Observer<any> = {
            next: (response: DataListResponse<Ministry>) => {
                this.items = response.data;
            },
            error: (error: HttpErrorResponse) => {
                console.error('Error:', error);
                if (error.status === 302) {
                    console.warn('Redirection detected, handling specific logic for status 302');
                    if (error.error && Array.isArray(error.error)) {
                    this.items = error.error as Ministry[];
                    }
                } else {
                    console.error(`Unhandled error status: ${error.status}`);
                }
                return throwError(() => error);
            },
            complete: () => { }
        }
        this.ministryService.getAll().subscribe(observer)
    }

    openNew() {
        this.item = {};
        this.submitted = false;
        this.itemDialog = true;
    }

    deleteSelectedItems() {
        this.deleteItemsDialog = true;
    }

    editItem(item: Ministry) {
        this.item = { ...item };
        this.itemDialog = true;
    }

    deleteItem(item: Ministry) {
        this.deleteItemDialog = true;
        this.item = { ...item };
    }

    confirmDeleteSelected() {
        this.deleteItemsDialog = false;
        this.items = this.items.filter(val => !this.selectedItems.includes(val));
        this.messageService.add({ severity: 'success', summary: 'Successful', detail: 'items Deleted', life: 3000 });
        this.selectedItems = [];
    }

    confirmDelete() {
        this.deleteItemDialog = false;
        this.items = this.items.filter(val => val.id !== this.item.id);
        this.messageService.add({ severity: 'success', summary: 'Successful', detail: 'item Deleted', life: 3000 });
        this.item = {};
    }

    hideDialog() {
        this.itemDialog = false;
        this.submitted = false;
    }

    saveItem() {
        this.submitted = true;
    
        if (this.item.id == null) {

            this.ministryService.create(this.item).subscribe(
                (response: DataResponse<Ministry>) => {
                    console.log('Ministère créé avec succès', response);
                    this.fetchMinistries();
                    this.item = {};
                    this.itemDialog = false;
                },
                (error) => {
                    console.error('Erreur lors de la création du ministère:', error);
                }
            );
        } else {
            this.ministryService.update(this.item.id, this.item).subscribe(
                (response: DataResponse<Ministry>) => {
                    console.log('Ministère mis à jour avec succès', response);
                    this.fetchMinistries();
                    this.item = {};
                    this.itemDialog = false;
                },
                (error) => {
                    console.error('Erreur lors de la mise à jour du ministère:', error);
                }
            );
        }
    }
    

    findIndexById(id: number): number {
        let index = -1;
        for (let i = 0; i < this.items.length; i++) {
            if (this.items[i].id === id) {
                index = i;
                break;
            }
        }

        return index;
    }

    onGlobalFilter(table: Table, event: Event) {
        table.filterGlobal((event.target as HTMLInputElement).value, 'contains');
    }
}
