import {UserDto} from './UserDto';

export interface RaidLeaderboard {
  scoreGold: number,
  scoreXp: number,
  scoreDate: Date,
  user: UserDto,
}

export interface LeaderBoard {
  scoreGold: number,
  scoreXp: number,
  scoreDate: Date,
  username: string,
}
