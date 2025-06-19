import { Component, OnInit } from '@angular/core';
import { DataListResponse, DataResponse } from 'src/app/interfaces/request-response';
import { MinistryService } from 'src/app/services/ministry.service';
import { Table } from 'primeng/table';
import { Observer, catchError, finalize, tap ,throwError } from 'rxjs';
import { CharacteristicsUsage } from 'src/app/interfaces/characteristics-usage';
import { CharacteristicsService } from 'src/app/services/characteristics.service';
import { ConfirmationService, MessageService } from 'primeng/api';

@Component({
  selector: 'app-characteristics',
  templateUrl: './characteristics.component.html',
  providers: [MessageService, ConfirmationService]
})
export class CharacteristicsComponent implements OnInit {
  itemDialog: boolean = false;
  deleteItemDialog: boolean = false;
  deleteItemsDialog: boolean = false;
  items: CharacteristicsUsage[] = [];
  selectedItems: CharacteristicsUsage[] = [];
  item: CharacteristicsUsage = {id: null,libLong: "",libCourt:"",unitPrice: 0};

  constructor(
    private characteristicsService: CharacteristicsService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService
  ) {}

  ngOnInit(): void {
    this.loadItems();
  }

  loadItems(): void {
    this.characteristicsService.getAll((data) => {
      this.items = data;
    });
  }

  openNew(): void {
    this.item = {id: null,libLong: "",libCourt:"",unitPrice: 0};
    this.itemDialog = true;
  }

  editItem(item: CharacteristicsUsage): void {
    this.item = { ...item };
    this.itemDialog = true;
  }

  deleteItem(item: CharacteristicsUsage) {
    this.deleteItemDialog = true;
    this.item = { ...item };
  }

  saveItem(): void {
    if (this.item.id) {
      this.update(this.item);
    } else {
      this.create();
    }
    this.itemDialog = false;
  }

  create(): void {
    this.characteristicsService.create(this.item, (data) => {
      this.items.push(data);
      this.item = { id : null ,libCourt: '', libLong: '', unitPrice: 0 };
      this.messageService.add({severity:'success', summary: 'Successful', detail: 'Type Usage Created', life: 3000});
    });
  }

  update(item: CharacteristicsUsage): void {
    if (item.id) {
      this.characteristicsService.update(item.id, item, (data) => {
        const index = this.items.findIndex(t => t.id === item.id);
        if (index !== -1) {
          this.items[index] = data;
        }
        this.messageService.add({severity:'success', summary: 'Successful', detail: 'Type Usage Updated', life: 3000});
      });
    }
  }

  hideDialog(): void {
    this.itemDialog = false;
  }

  deleteSelectedItems() {
    this.deleteItemsDialog = true;
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
    this.item = {id: null,libLong: "",libCourt:"",unitPrice: 0};
}
}
