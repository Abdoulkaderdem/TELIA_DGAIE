import { Component, OnInit } from '@angular/core';
import { ConfirmationService, MessageService } from 'primeng/api';
import { TypeUsageService } from 'src/app/services/type-usage.service';
import { TypeUsage } from 'src/app/interfaces/type-usage';
import { createGenericObserver } from 'src/app/observers/generics';

@Component({
  selector: 'app-type-usage',
  templateUrl: './type-usage.component.html',
  providers: [MessageService, ConfirmationService]
})
export class TypeUsageComponent implements OnInit {
  typeUsages: TypeUsage[] = [];
  typeUsageDialog: boolean = false;
  typeUsage: TypeUsage = {id: null, libCourt: '', libLong: ''};
  selectedTypeUsages: TypeUsage[] = [];

  constructor(
    private typeUsageService: TypeUsageService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService
  ) {}

  ngOnInit(): void {
    this.loadTypeUsages();
  }

  loadTypeUsages(): void {
    this.typeUsageService.getTypeUsages((data) => {
      this.typeUsages = data;
      console.log(data);
    });
  }

  openNew(): void {
    this.typeUsage = {id: null, libCourt: '', libLong: ''};
    this.typeUsageDialog = true;
  }

  editTypeUsage(typeUsage: TypeUsage): void {
    this.typeUsage = {...typeUsage};
    this.typeUsageDialog = true;
  }

  deleteTypeUsage(typeUsage: TypeUsage): void {
    this.confirmationService.confirm({
      message: 'Are you sure you want to delete ' + typeUsage.libCourt + '?',
      accept: () => {
        /*this.typeUsageService.delete(typeUsage.id).subscribe(() => {
          this.typeUsages = this.typeUsages.filter(val => val.id !== typeUsage.id);
          this.messageService.add({severity:'success', summary: 'Successful', detail: 'TypeUsage Deleted'});
        });*/
      }
    });
  }

  saveTypeUsage(): void {
    if (this.typeUsage.id) {
      this.updateTypeUsage(this.typeUsage);
    } else {
      this.createTypeUsage();
    }
    this.typeUsageDialog = false;
  }

  createTypeUsage(): void {
    this.typeUsageService.createTypeUsage(this.typeUsage, (data) => {
      this.typeUsages.push(data);
      this.typeUsage = { id : null ,libCourt: '', libLong: '' };
      this.messageService.add({severity:'success', summary: 'Successful', detail: 'Type Usage Created', life: 3000});
    });
  }

  updateTypeUsage(typeUsage: TypeUsage): void {
    if (typeUsage.id) {
      this.typeUsageService.updateTypeUsage(typeUsage.id, typeUsage, (data) => {
        const index = this.typeUsages.findIndex(t => t.id === typeUsage.id);
        if (index !== -1) {
          this.typeUsages[index] = data;
        }
        this.messageService.add({severity:'success', summary: 'Successful', detail: 'Type Usage Updated', life: 3000});
      });
    }
  }

  hideDialog(): void {
    this.typeUsageDialog = false;
  }
}
