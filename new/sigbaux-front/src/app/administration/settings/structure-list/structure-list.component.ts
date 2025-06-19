import { Component, OnInit } from '@angular/core';
import { MinisterialStructure } from 'src/app/interfaces/ministerial-structure';
import { Ministry } from 'src/app/interfaces/ministry';
import { DataListResponse, DataResponse } from 'src/app/interfaces/request-response';
import { StructureService } from 'src/app/services/ministerial-structure.service';
import { MinistryService } from 'src/app/services/ministry.service';
import { ConfirmationService, MessageService } from 'primeng/api';
import { SelectItem } from 'primeng/api';
import { Table } from 'primeng/table';
import { LayoutService } from 'src/app/layout/service/app.layout.service';
import { Observer, catchError, finalize, tap ,throwError } from 'rxjs';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { DropdownChangeEvent } from 'primeng/dropdown';


@Component({
  selector: 'app-structure',
  templateUrl: './structure-list.component.html',
  providers: [MessageService, ConfirmationService]
})
export class StructureList implements OnInit {

  itemDialog: boolean = false;

  deleteItemDialog: boolean = false;

  deleteItemsDialog: boolean = false;

  items: MinisterialStructure[] = [];

  ministeres: Ministry[] = [];

  item: MinisterialStructure = this.initialStructure();

  selectedItems: MinisterialStructure[] = [];

  submitted: boolean = false;

  cols: any[] = [];

  statuses: any[] = [];

  ministeresOptions: SelectItem[] = [];

  selectedDrop: SelectItem = { value: '' };

  rowsPerPageOptions = [5, 10, 20];

  constructor(
    private structureService: StructureService, 
    private ministryService: MinistryService, 
    private messageService: MessageService
  ) { }

  ngOnInit() {
    this.fetchMinistries();
    this.fetchStructures();
    this.cols = [
      { field: 'name', header: 'Nom' },
      { field: 'idMinistry', header: 'Ministère' },
      { field: 'domain', header: 'Domaine' },
      { field: 'phone', header: 'Téléphone' },
      { field: 'email', header: 'Email' },
      { field: 'manager', header: 'Ordonanceur' },
      { field: 'code', header: 'Code' }
    ];
  }

  fetchStructures(): void {
    this.structureService.getAll()
      .pipe(
        catchError((error: HttpErrorResponse) => {
          console.error('Error:', error);

          if (error.status === 302) {
            console.warn('Redirection detected, handling specific logic for status 302');
            if (error.error && Array.isArray(error.error)) {
              this.items = error.error as MinisterialStructure[];
            }
          } else {
            console.error(`Unhandled error status: ${error.status}`);
          }
          return throwError(() => error);
        })
      )
      .subscribe(
        (response: DataListResponse<MinisterialStructure>) => {
          this.items = response.data;
        },
        (error) => {
          console.error('Subscription error:', error);
        }
      );
  }

  fetchMinistries(): void {
    this.ministryService.getAll()
      .pipe(
        catchError((error: HttpErrorResponse) => {
          console.error('Error:', error);

          if (error.status === 302) {
            console.warn('Redirection detected, handling specific logic for status 302');
            if (error.error && Array.isArray(error.error)) {
              this.ministeres = error.error as Ministry[];
              this.ministeresOptions = this.ministeres.map(ministere => ({
                label: ministere.name,
                value: ministere.id
              }));
            }
          } else {
            console.error(`Unhandled error status: ${error.status}`);
          }
          return throwError(() => error);
        })
      )
      .subscribe(
        (response: DataListResponse<Ministry>) => {
          this.ministeres = response.data;
          this.ministeresOptions = this.ministeres.map(ministere => ({
            label: ministere.name,
            value: ministere.id
          }));
        },
        (error) => {
          console.error('Subscription error:', error);
        }
      );
  }

  openNew() {
    this.item = this.initialStructure();
    this.fetchMinistries();
    this.submitted = false;
    this.itemDialog = true;
  }

  deleteSelectedItems() {
    this.deleteItemsDialog = true;
  }

  editItem(item: MinisterialStructure) {
    this.item = { ...item };
    this.selectedDrop = { value: item.manager };
    this.itemDialog = true;
  }

  deleteItem(item: MinisterialStructure) {
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
    this.item = this.initialStructure();
  }

  hideDialog() {
    this.itemDialog = false;
    this.submitted = false;
  }

  saveItem() {
    this.submitted = true;

    if (this.item.id == null) {
      this.structureService.create(this.item).subscribe(
        (response: DataResponse<MinisterialStructure>) => {
          console.log('Ministère créé avec succès', response);
          this.fetchStructures();
          this.item = this.initialStructure();
          this.itemDialog = false;
        },
        (error) => {
          console.error('Erreur lors de la création du ministère:', error);
        }
      );
    } else {
      this.structureService.update(this.item.id, this.item).subscribe(
        (response: DataResponse<MinisterialStructure>) => {
          console.log('Ministère mis à jour avec succès', response);
          this.fetchStructures();
          this.item = this.initialStructure();
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

  getMinistryName(id: number) {
    const ministry = this.ministeres.find(m => m.id === id);
    return ministry ? ministry!.name : " - ";
  }

  onGlobalDropdownChangeFilter(table: Table, event: DropdownChangeEvent) {
    table.filterGlobal((event.value as HTMLInputElement).value, 'contains');
  }

  onGlobalFilter(table: Table, event: Event) {
    table.filterGlobal((event.target as HTMLInputElement).value, 'contains');
  }

  initialStructure(): MinisterialStructure {
    return {
      id: null,
      name: '',
      domain: '',
      phone: '',
      email: '',
      manager: null,
      code: '',
      idMinistry: 0
    };
  }
}

    
