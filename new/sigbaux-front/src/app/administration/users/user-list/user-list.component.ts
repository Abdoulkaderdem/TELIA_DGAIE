import { Component, OnInit } from '@angular/core';
import { User } from 'src/app/interfaces/user';
import { DataListResponse, DataResponse } from 'src/app/interfaces/request-response';
import { UserService } from 'src/app/services/user.service';
import { ConfirmationService, MessageService } from 'primeng/api';
import { Table } from 'primeng/table';
import { LayoutService } from 'src/app/layout/service/app.layout.service';
import { TypeUser, RoleType } from 'src/app/interfaces/enumerations';


@Component({
    templateUrl: './user-list.component.html',
    providers: [MessageService, ConfirmationService]
})
export class UserList implements OnInit {

    userDialog: boolean = false;

    deleteUserDialog: boolean = false;

    deleteUsersDialog: boolean = false;

    password: string = "";

    users: User[] = [];

    user: User = {
        id: null,
        firstName: null,
        lastName: null,
        email: null,
        password: null,
        matricule: null,
        typeUser: null,
        role: null
    };

    selectedUsers: User[] = [];

    submitted: boolean = false;

    cols: any[] = [];

    types: any[] = [];

    roles: any[] = [];

    rowsPerPageOptions = [5, 10, 20];

    constructor(
        private userService: UserService, 
        private messageService: MessageService, 
        private confirmationService: ConfirmationService, 
        private layoutService: LayoutService
    ) { }

    ngOnInit() {

        this.loadUsers();

        this.cols = [
            { field: 'matricule', header: 'Matricule' },
            { field: 'firstName', header: 'First Name' },
            { field: 'lastName', header: 'Last Name' },
            { field: 'email', header: 'Email' },
            { field: 'typeUser', header: 'User Type' },
            { field: 'role', header: 'Role' }
        ];

        /*this.types = [
            { label: 'Admin', value: TypeUser.ADMIN },
            { label: 'User', value: TypeUser.USER },
            { label: 'Guest', value: TypeUser.GUEST },
            { label: 'Dgaie', value: TypeUser.DGAIE },
            { label: 'Public', value: TypeUser.PUBLIC }
        ];*/
        this.types = [
            { label: 'Dgaie', value: TypeUser.DGAIE },
            { label: 'Public', value: TypeUser.PUBLIC }
        ];

        this.roles = [
            { label: 'Super Admin', value: RoleType.SUPER_ADMIN },
            { label: 'Admin', value: RoleType.ADMIN },
            { label: 'Editor', value: RoleType.EDITOR },
            { label: 'CPM', value: RoleType.CPM },
            { label: 'Viewer', value: RoleType.VIEWER },
            { label: 'Daie', value: RoleType.DAIE },
            { label: 'Dsi', value: RoleType.DSI },

            

        ];
        /*this.roles = [
            { label: 'Daie', value: RoleType.DAIE },
            { label: 'Dsi', value: RoleType.DSI },
        ];*/
    }

    loadUsers(): void {
        this.userService.getUsers((data) => {
            this.users = data;
        });
    }

    openNew() {
        this.user = this.initUser();
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

    saveUser(): void {
        this.submitted = true;
        if (this.user.id) {
          this.updateUser(this.user);
        } else {
          this.createUser();
        }
        this.userDialog = false;
      }
    
      createUser(): void {
        this.userService.createUser(this.user,(data) => {
            this.users.push(data);
            this.messageService.add(
                { 
                    severity: 'success', 
                    summary: 'Successful', 
                    detail: 'User Created', 
                    life: 3000 
                }
            );
            this.user = this.initUser();
        });
      }
    
      updateUser(user: User): void {
        this.userService.updateUser(user, (data) => {
            this.users[this.findIndexById(this.user.id!)] = user;
            this.messageService.add(
                { 
                    severity: 'success', 
                    summary: 'Successful', 
                    detail: 'User Updated', 
                    life: 3000 
                }
            );
            this.user = this.initUser();
        });
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

    initUser(){
        return {
            id: null,
            firstName: null,
            lastName: null,
            email: null,
            password: null,
            matricule: null,
            typeUser: null,
            role: null
        };
    }
}
