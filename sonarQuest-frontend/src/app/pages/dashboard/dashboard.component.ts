import {Component, OnInit} from '@angular/core';
import {Raid} from '../../Interfaces/Raid';
import {Monster} from '../../Interfaces/Monster';
import {Task} from '../../Interfaces/Task';
import {Quest} from '../../Interfaces/Quest';
import {LeaderBoard} from '../../Interfaces/RaidLeaderboard';
import {ActivatedRoute, Router} from '@angular/router';
import {RaidService} from '../../services/raid.service';
import {EventService} from '../../services/event.service';
import {QuestService} from '../../services/quest.service';
import {WorldService} from '../../services/world.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  public raid: Raid;
  public monster: Monster;
  public tasks: Task[] = [];
  public quests: Quest[] = [];
  public leaderboard: LeaderBoard[] = [];


  constructor(private route: ActivatedRoute, private router: Router, private raidService: RaidService,
              private questService: QuestService, private eventService: EventService,
              private worldService: WorldService) {
  }
  ngOnInit() {
    const _this = this;
    this.worldService.currentWorld$.subscribe(world => {
      console.log(world);
      _this.leaderboard = world.leaderboard;
    })
  }
/*  private sortLeaderBoard() {
    this.leaderboard.sort((score1, score2) => {
      if (score1.scoreXp > score2.scoreXp) {
        return -1;
      }
      if (score1.scoreXp < score2.scoreXp) {
        return 1;
      }
      return 0;
    });
  }*/
}
