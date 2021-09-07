import {Component, Input, OnInit} from '@angular/core';
import {UserService} from '../../../services/user.service';
import {MatDialog} from '@angular/material';
import {EventPageComponent} from '../../event-page/event-page.component';
import {LeaderBoard} from '../../../Interfaces/RaidLeaderboard';
import {ITdDataTableColumn} from '@covalent/core';


@Component({
  selector: 'app-highscore',
  templateUrl: './highscore.component.html',
  styleUrls: ['./highscore.component.css']
})
export class HighscoreComponent implements OnInit {

  @Input()
  leaderboard: LeaderBoard[];

  columns: ITdDataTableColumn[] = [
    {name: 'username', label: 'Name'},
    {name: 'scoreXp', label: 'XP'},
    {name: 'scoreGold', label: 'Gold'}
  ]


  ngOnInit() {
    this.sortLeaderBoard();
  }

  private sortLeaderBoard() {
    this.leaderboard.sort((score1, score2) => {
      if (score1.scoreXp > score2.scoreXp) {
        return -1;
      }
      if (score1.scoreXp < score2.scoreXp) {
        return 1;
      }
      return 0;
    });
  }

  constructor(private userService: UserService,
              private dialog: MatDialog,
  ) {

  }

  openChat() {
    this.dialog.open(EventPageComponent, {
      panelClass: 'dialog-sexy',
      width: '300px',
      height: '500px'
    })
  }
}
