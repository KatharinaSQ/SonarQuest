package com.viadee.sonarquest.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.viadee.sonarquest.interfaces.LeaderboardScore;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table
public class Leaderboard implements Comparable<Leaderboard>, LeaderboardScore {

   @Id
    @GeneratedValue
    private Long id;

    @Column(name = "scoreDay")
    private Date scoreDay;

    @Column(name = "scoreGold")
    private Long scoreGold;

    @Column(name = "scoreXp")
    private Long scoreXp;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "world_id")
    private World world;

    public Leaderboard() {
    }

    public Leaderboard(User user, World world ) {
        this.user = user;
        this.world = world;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }


    public String getUsername() {
        if(user==null)
            return "";
        return user.getUsername();
    }


    public void setUser(User user) {
        this.user = user;
    }

    public Long getScoreGold() {
        if(scoreGold==null)
            this.scoreGold = 0l;
        return scoreGold;
    }

    public void setScoreGold(Long scoreGold) {
        this.scoreGold = scoreGold;
    }

    public Long getScoreXp() {
        if(scoreXp==null)
            this.scoreXp = 0l;
        return scoreXp;
    }

    public void setScoreXp(Long scoreXp) {
        this.scoreXp = scoreXp;
    }

    public void addScoreXp(Long scoreXp) {
        if(this.scoreXp==null)
            this.scoreXp = 0l;
        this.scoreXp += scoreXp;
    }

    public void addScoreGold(Long scoreGold) {
        if(this.scoreGold==null)
            this.scoreGold = 0l;
        this.scoreGold += scoreGold;
    }

    public Date getScoreDay() {
        return scoreDay;
    }

    public void setScoreDay(Date scoreDay) {
        this.scoreDay = scoreDay;
    }
    @JsonIgnore
    @Override
    public int compareTo(Leaderboard o) {
        return (int) (this.getScoreXp() - o.getScoreXp());
    }
}
