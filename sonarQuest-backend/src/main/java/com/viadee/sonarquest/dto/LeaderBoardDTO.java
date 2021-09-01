package com.viadee.sonarquest.dto;

import com.viadee.sonarquest.entities.Leaderboard;
import com.viadee.sonarquest.entities.UserDto;

import java.sql.Date;

public class LeaderBoardDTO {

    private Date scoreDay;
    private Long scoreGold;
    private Long scoreXp;
    private UserDto user;

    public LeaderBoardDTO(Leaderboard leaderBoard) {
        this(leaderBoard.getScoreGold(), leaderBoard.getScoreXp(), new UserDto(leaderBoard.getUser()),
                leaderBoard.getScoreDay());
    }

    public LeaderBoardDTO(Long scoreGold, Long scoreXp, UserDto user, Date scoreDay) {
        this.scoreGold = scoreGold;
        this.scoreXp = scoreXp;
        this.user = user;
        this.scoreDay = scoreDay;
    }

    public Long getScoreGold() {
        return scoreGold;
    }

    public void setScoreGold(Long scoreGold) {
        this.scoreGold = scoreGold;
    }

    public Long getScoreXp() {
        return scoreXp;
    }

    public void setScoreXp(Long scoreXp) {
        this.scoreXp = scoreXp;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public Date getScoreDay() {
        return scoreDay;
    }

    public void setScoreDay(Date scoreDay) {
        this.scoreDay = scoreDay;
    }

}
