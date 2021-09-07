import {Component, OnDestroy, OnInit} from '@angular/core';
import {StandardTaskService} from '../../../services/standard-task.service';
import {WorldService} from '../../../services/world.service';
import {World} from '../../../Interfaces/World';
import {QuestService} from '../../../services/quest.service';
import {Subscription} from 'rxjs';
import {SolvedTaskHistoryDto} from '../../../Interfaces/SolvedTaskHistoryDto';


@Component({
  selector: 'app-progress',
  templateUrl: './progress.component.html',
  styleUrls: ['./progress.component.css']
})

export class ProgressComponent implements OnInit, OnDestroy {
  historyList: SolvedTaskHistoryDto [] = [];
  taskList: number;


  public barChartOptions = {
    scaleShowVerticalLines: false,
    responsive: true,
    scales: {
      yAxes: [{
        ticks: {
          beginAtZero: true
        }
      }],
    }
  }
  public barChartLabels = [];
  public barChartType = 'bar';
  public barChartLegend = true;
  public barChartData = [];


  questSub: Subscription;

  constructor(private standardTaskService: StandardTaskService,
              private worldService: WorldService,
              private questService: QuestService,
  ) {
  }

  ngOnInit() {
    const _this = this;
    this.worldService.currentWorld$.subscribe(world => {
      _this.questSub = _this.questService.getSolvedTaskHistoryListForAllQuests(world.id).subscribe(history => {
        _this.historyList = history;
        this.standardTaskService.getStandardTasksForWorld(world).subscribe(taskList => {
          this.taskList = taskList.length;
          this.loadTasks(world);
        })
        _this.setDateArray();

      })
    })
  }

  ngOnDestroy(): void {
    this.questSub.unsubscribe();
  }

  public loadTasks(world: World) {
    const _this = this;
    this.barChartData = [{data: _this.historyList.map(e => e.solvedTasksForDay), label: ' Täglich gelöste Issues'},
      {data: _this.historyList.map(e => e.totalSolvedTasks), label: 'Alle gelösten Issues'}];
    /*     {data: _this.taskList, label: 'Offene Issues'}];*/
  }

  public setDateArray() {
    let i: number;
    for (i = 0; i < this.historyList.length; i++) {
      this.barChartLabels[i] = this.historyList[i].date;
    }
  }
}
