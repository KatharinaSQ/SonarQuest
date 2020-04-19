package com.viadee.sonarquest.entities;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table
public class RaidLeaderboard implements Comparable<RaidLeaderboard> {
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name = "scoreDay")
	private Date scoreDay;
	
	@Column(name = "scoreSolvedTasks")
	private Long scoreSolvedTasks;

	@Column
	private Long scoreGold;

	@Column
	private Long scoreXp;

	@JsonIgnore
	@ManyToOne(optional = false)
	@JoinColumn(name = "raid_id")
	private Raid raid;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	public RaidLeaderboard() {
	}
	
	public RaidLeaderboard(User user, Raid raid) {
		this.user = user;
		this.raid = raid;
	}

	public Raid getRaid() {
		return raid;
	}

	public void setRaid(Raid raid) {
		this.raid = raid;
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

	public Long getScoreSolvedTasks() {
		return scoreSolvedTasks;
	}

	public void setScoreSolvedTasks(Long solvedTasks) {
		this.scoreSolvedTasks = solvedTasks;
	}

	public void addScoreSolvedTasks(long scorePoints) {
		if(this.scoreSolvedTasks==null) {
			this.scoreSolvedTasks = 0l;
		}
		this.scoreSolvedTasks += scorePoints;
	}

	@JsonIgnore
	@Override
	public int compareTo(RaidLeaderboard o) {
		return (int) (this.getScoreXp() - o.getScoreXp());
	}
}
