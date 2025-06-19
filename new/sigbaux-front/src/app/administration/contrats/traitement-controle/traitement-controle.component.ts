import { Component, OnInit } from '@angular/core';
import { ConfirmationService, MessageService } from 'primeng/api';
import { ContractService } from 'src/app/services/contrat.service';
import { ContractDto, ContractResponse, ContractRevisionResponse } from 'src/app/interfaces/contrat';
import { BuildingWithProvisionalRentAmount } from 'src/app/interfaces/building-with-provisional-rent-amount';

@Component({
  selector: 'app-traitement-controle',
  templateUrl: './traitement-controle.component.html',
  providers: [MessageService, ConfirmationService]
})
export class TraitementControleComponent implements OnInit {
  contracts: ContractResponse[] = [];
  contractDialog: boolean = false;
  contractRevisionDialog: boolean = false;
  buildings: BuildingWithProvisionalRentAmount[] = [];
  contract: ContractDto = this.initializeContract();
  revisionDetails: string = '';
  selectedContracts: ContractResponse[] = [];
  selectedBuilding: BuildingWithProvisionalRentAmount | undefined;

  constructor(
    private contractService: ContractService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService
  ) {}

  ngOnInit(): void {
    this.loadContracts();
    this.loadBuildings();
  }

  initializeContract(): ContractDto {
    return { 
      id: undefined, 
      buildingId: 0, 
      rentAmount: 0, 
      startDate: '', 
      endDate: '', 
      terms: '', 
      contractPeriodicity: '',
      bankAccountNumber: '',
      president: '',
      reporter: '',
      contractingAuthority: ''
    };
  }

  loadContracts(): void {
    this.contractService.getAllContracts().subscribe((data) => {
      this.contracts = data;
    });
  }

  loadBuildings(): void {
    this.contractService.getBuildingsWithProvisionalRentAmount().subscribe((data) => {
      this.buildings = data;
    });
  }

  openNew(): void {
    this.contract = this.initializeContract();
    this.contractDialog = true;
  }

  editContract(contract: ContractResponse): void {
    this.contract = { ...contract };
    this.contractDialog = true;
  }

  deleteContract(contract: ContractResponse): void {
    this.confirmationService.confirm({
      message: 'Are you sure you want to terminate this contract?',
      accept: () => {
        this.contractService.terminateContract(contract.id,'motif').subscribe(() => {
          this.contracts = this.contracts.filter(val => val.id !== contract.id);
          this.messageService.add({ severity: 'success', summary: 'Successful', detail: 'Contract Terminated' });
        });
      }
    });
  }

  saveContract(): void {
    if (this.contract.id) {
      this.updateContract(this.contract);
    } else {
      this.createContract();
    }
    this.contractDialog = false;
  }

  createContract(): void {
    this.contractService.createContract(this.contract).subscribe((data) => {
      this.contracts.push(data);
      this.messageService.add({ severity: 'success', summary: 'Successful', detail: 'Contract Created', life: 3000 });
    });
  }

  updateContract(contract: ContractDto): void {
    this.contractService.updateContract(contract.id!, contract).subscribe((data) => {
      const index = this.contracts.findIndex(c => c.id === contract.id);
      if (index !== -1) {
        this.contracts[index] = data;
      }
      this.messageService.add({ severity: 'success', summary: 'Successful', detail: 'Contract Updated', life: 3000 });
    });
  }

  hideDialog(): void {
    this.contractDialog = false;
  }

  addRevision(contract: ContractResponse): void {
    this.contract = { ...contract };
    this.revisionDetails = '';
    this.contractRevisionDialog = true;
  }

  saveRevision(): void {
    if (this.contract.id && this.revisionDetails) {
      this.contractService.addContractRevision(this.contract.id, this.revisionDetails).subscribe((data) => {
        this.messageService.add({ severity: 'success', summary: 'Successful', detail: 'Contract Revision Added', life: 3000 });
      });
    }
    this.contractRevisionDialog = false;
  }

  setProvisionalRentAmount(): void {
    if (this.selectedBuilding && this.selectedBuilding.provisionalRentAmount) {
      this.contractService.provisionalRentAmount(this.selectedBuilding.idBuilding, this.selectedBuilding.provisionalRentAmount).subscribe(() => {
        this.messageService.add({ severity: 'success', summary: 'Successful', detail: 'Provisional Rent Amount Set', life: 3000 });
        this.loadBuildings();
      });
    }
  }
}
