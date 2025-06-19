import { Component, OnInit } from '@angular/core';
import { RequestService } from 'src/app/services/request.service';
import { Request } from 'src/app/models/request.model';
import { MinistryService } from 'src/app/services/ministry.service';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observer, catchError, finalize, tap ,throwError } from 'rxjs';
import { Ministry } from 'src/app/interfaces/ministry';
import { DataListResponse, DataResponse } from 'src/app/interfaces/request-response';

@Component({
  selector: 'app-registre',
  templateUrl: './registre.component.html',
  styleUrl: './registre.component.css'
})
export class RegistreComponent {
  requests: Request[] = [];
  ministeres: Ministry[] = [];

  constructor(
    private requestService: RequestService,
    private ministryService: MinistryService, 
  ) {}

  ngOnInit() {
    this.requestService.getRequestsByStatus("NEW").subscribe(data => (this.requests = data));
    this.fetchMinistries();
  }

  onTabChange(event: any): void {
    const tabIndex = event.index;
    switch (tabIndex) {
      case 0:
        this.refreshList("NEW");
        break;
      case 1:
        this.refreshList("VALIDATE");
        break;
      case 2:
        this.refreshList("SEND");
        break;
      case 3:
        this.refreshList("APPROVAL");
        break;
      case 4:
        this.refreshList("COMPLEMENT");
        break;
      case 5:
        this.refreshList("REJECTED");
        break;
      case 6:
        this.getAll();
        break;
    }
  }

  refreshList(status: string): void {
    this.requestService.getRequestsByStatus(status).subscribe(data => (this.requests = data));
  }

  getAll(){
    this.requestService.getRequests().subscribe(data => (this.requests = data));
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
        },
        (error) => {
          console.error('Subscription error:', error);
        }
      );
  }
}
