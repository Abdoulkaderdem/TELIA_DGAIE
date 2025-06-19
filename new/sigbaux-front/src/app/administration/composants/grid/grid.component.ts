import { Component, Input} from '@angular/core';
import { CommonModule } from '@angular/common';
import { CardComponent } from '../card/card.component';

@Component({
  selector: 'app-grid',
  standalone: true,
  imports: [
    CardComponent,
    CommonModule
  ],
  templateUrl: './grid.component.html',
  styleUrls: ['./grid.component.css']
})
export class GridComponent {
  @Input() cards: any[] = [];
}
