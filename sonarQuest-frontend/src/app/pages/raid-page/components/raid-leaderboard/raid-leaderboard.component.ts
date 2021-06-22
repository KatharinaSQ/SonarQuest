import {Component, Input, OnInit} from '@angular/core';
import {RaidLeaderboard} from 'app/Interfaces/RaidLeaderboard';
import {ITdDataTableColumn} from '@covalent/core';

@Component({
  selector: 'app-raid-leaderboard',
  templateUrl: './raid-leaderboard.component.html',
  styleUrls: ['./raid-leaderboard.component.css']
})
export class RaidLeaderboardComponent implements OnInit {
  @Input()
  raidLeaderboard: RaidLeaderboard[];

  columns: ITdDataTableColumn[] = [
    {name: 'user.username', label: 'Name'},
    {name: 'scoreXp', label: 'XP'},
    {name: 'scoreGold', label: 'Gold'}
  ]

  constructor() {
  }

  ngOnInit() {
    this.raidLeaderboard = this.raidLeaderboard.sort((score1, score2) => {
      if (score1.scoreXp > score2.scoreXp) {
        return 1;
      }
      if (score1.scoreXp < score2.scoreXp) {
        return -1;
      }
      return 0;
    });
  }

}
