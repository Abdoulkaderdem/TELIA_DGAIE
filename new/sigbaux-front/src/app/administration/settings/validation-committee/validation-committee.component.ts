import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ValidationCommitteeService } from 'src/app/services/validation-committee';
import { DataListResponse, DataResponse } from 'src/app/interfaces/request-response';
import { ValidationCommittee } from 'src/app/interfaces/comitte';
import { MessageService, ConfirmationService } from 'primeng/api';

@Component({
  selector: 'app-validation-committee',
  templateUrl: './validation-committee.component.html',
  providers: [MessageService, ConfirmationService]
})
export class ValidationCommitteeComponent implements OnInit {
  committees: ValidationCommittee[] = [];
  committeeForm: FormGroup;
  selectedCommittee: ValidationCommittee | null = null;
  dialogVisible: boolean = false;

  constructor(
    private fb: FormBuilder,
    private committeeService: ValidationCommitteeService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService
  ) {
    this.committeeForm = this.fb.group({
      id: [null],
      name: ['', Validators.required],
      members: this.fb.array([]),
      responsible: this.fb.group({
        id: [null],
        firstName: ['', Validators.required],
        lastName: ['', Validators.required],
        matricule: ['', Validators.required],
        phoneNumber: ['', Validators.required],
        email: ['', [Validators.required, Validators.email]],
        function: ['', Validators.required],
      }),
      rentalRequests: [[]]
    });
  }

  ngOnInit(): void {
    this.loadCommittees();
  }

  loadCommittees(): void {
    this.committeeService.getAllCommittees().subscribe((response: DataListResponse<ValidationCommittee>) => {
      this.committees = response.data;
    });
  }

  openNew(): void {
    this.selectedCommittee = null;
    this.committeeForm.reset();
    this.dialogVisible = true;
  }

  editCommittee(committee: ValidationCommittee): void {
    this.selectedCommittee = committee;
    this.committeeForm.patchValue(committee);
    this.dialogVisible = true;
  }

  deleteCommittee(committee: ValidationCommittee): void {
    this.confirmationService.confirm({
      message: 'Are you sure you want to delete this committee?',
      accept: () => {
        // Call delete service here if it exists
        this.committees = this.committees.filter(val => val.id !== committee.id);
        this.messageService.add({severity:'success', summary: 'Successful', detail: 'Committee Deleted', life: 3000});
      }
    });
  }

  saveCommittee(): void {
    if (this.committeeForm.valid) {
      const committee: ValidationCommittee = this.committeeForm.value;
      if (committee.id) {
        this.committeeService.updateCommittee(committee).subscribe((response: DataResponse<ValidationCommittee>) => {
          this.loadCommittees();
          this.dialogVisible = false;
          this.messageService.add({severity:'success', summary: 'Successful', detail: 'Committee Updated', life: 3000});
        });
      } else {
        this.committeeService.createCommittee(committee).subscribe((response: DataResponse<ValidationCommittee>) => {
          this.loadCommittees();
          this.dialogVisible = false;
          this.messageService.add({severity:'success', summary: 'Successful', detail: 'Committee Created', life: 3000});
        });
      }
    } else {
      this.messageService.add({severity:'error', summary: 'Error', detail: 'Please fill in all required fields', life: 3000});
    }
  }

  hideDialog(): void {
    this.dialogVisible = false;
  }
}
