import {Component, Input, OnInit} from '@angular/core';
import {Quest} from '../../Interfaces/Quest';
import {QuestService} from '../../services/quest.service';
import {WorldService} from '../../services/world.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  @Input()
  public allQuest: Quest [];

  constructor(private questService: QuestService, private worldService: WorldService) {
  }

  ngOnInit() {
  }

}
