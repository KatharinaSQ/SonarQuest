import {Component, OnInit} from '@angular/core';
import {WorldService} from '../../services/world.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  constructor(private worldService: WorldService) {
  }

  ngOnInit() {
  }

}
