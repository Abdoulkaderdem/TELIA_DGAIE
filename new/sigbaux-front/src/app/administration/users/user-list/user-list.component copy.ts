import { Component, OnInit } from '@angular/core';
import { User } from 'src/app/interfaces/user';
import { DataListResponse, DataResponse } from 'src/app/interfaces/request-response';
import { UserService } from 'src/app/services/user.service';
import { ConfirmationService, MessageService } from 'primeng/api';
import { Table } from 'primeng/table';
import { LayoutService } from 'src/app/layout/service/app.layout.service';

@Component({
    templateUrl: './user-list.component.html',
    providers: [MessageService, ConfirmationService]
})
export class UserList implements OnInit {

    userDialog: boolean = false;

    deleteUserDialog: boolean = false;

    deleteUsersDialog: boolean = false;

    users: User[] = [];

    user: User = {} as User;

    selectedUsers: User[] = [];

    submitted: boolean = false;

    cols: any[] = [];

    rowsPerPageOptions = [5, 10, 20];

    constructor(
        private userService: UserService,
        private messageService: MessageService,
        private confirmationService: ConfirmationService,
        private layoutService: LayoutService
    ) { }

    ngOnInit() {
        this.userService.getAll().subscribe(data => this.users = data.data);

        this.cols = [
            { field: 'firstName', header: 'First Name' },
            { field: 'lastName', header: 'Last Name' },
            { field: 'email', header: 'Email' },
            { field: 'typeUser', header: 'User Type' },
            { field: 'role', header: 'Role' }
        ];
    }

    openNew() {
        this.user = {} as User;
        this.submitted = false;
        this.userDialog = true;
    }

    deleteSelectedUsers() {
        this.deleteUsersDialog = true;
    }

    editUser(user: User) {
        this.user = { ...user };
        this.userDialog = true;
    }

    deleteUser(user: User) {
        this.deleteUserDialog = true;
        this.user = { ...user };
    }

    confirmDeleteSelected() {
        this.deleteUsersDialog = false;
        this.users = this.users.filter(val => !this.selectedUsers.includes(val));
        this.messageService.add({ severity: 'success', summary: 'Successful', detail: 'Users Deleted', life: 3000 });
        this.selectedUsers = [];
    }

    confirmDelete() {
        this.deleteUserDialog = false;
        this.users = this.users.filter(val => val.id !== this.user.id);
        this.messageService.add({ severity: 'success', summary: 'Successful', detail: 'User Deleted', life: 3000 });
        this.user = {} as User;
    }

    hideDialog() {
        this.userDialog = false;
        this.submitted = false;
    }

    saveUser() {
        this.submitted = true;

        if (this.user.firstName?.trim() && this.user.lastName?.trim() && this.user.email?.trim()) {
            if (this.user.id) {
                this.users[this.findIndexById(this.user.id)] = this.user;
                this.messageService.add({ severity: 'success', summary: 'Successful', detail: 'User Updated', life: 3000 });
            } else {
                this.user.id = this.createId();
                this.users.push(this.user);
                this.messageService.add({ severity: 'success', summary: 'Successful', detail: 'User Created', life: 3000 });
            }

            this.users = [...this.users];
            this.userDialog = false;
            this.user = {} as User;
        }
    }

    findIndexById(id: number): number {
        return this.users.findIndex(user => user.id === id);
    }

    createId(): number {
        return Math.floor(Math.random() * 1000000);
    }

    onGlobalFilter(table: Table, event: Event) {
        table.filterGlobal((event.target as HTMLInputElement).value, 'contains');
    }
}
