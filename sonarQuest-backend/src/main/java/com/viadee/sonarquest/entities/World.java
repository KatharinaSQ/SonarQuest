package com.viadee.sonarquest.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Objects;

@Entity
@Table(name = "World")
public class World {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "project")
    private String project;
    
    @Column(name="branch")
    private String branch;

    @Column(name = "image")
    private String image;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "usequestcards")
    private Boolean usequestcards;
    
    @Column(name="filter")
    private String filter;

    @JsonIgnore
    @OneToMany(mappedBy = "world")
    private List<Adventure> adventures;

    @JsonIgnore
    @OneToMany(mappedBy = "world")
    private List<Quest> quests;

    @JsonIgnore
    @OneToMany(mappedBy = "world", cascade = CascadeType.ALL)
    private List<Task> tasks;

    @OneToMany(mappedBy = "world", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Leaderboard> leaderboard;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "User_To_World", joinColumns = @JoinColumn(name = "world_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private List<User> users;

    public World() {
    }

    public World(final String name, final String project, final Boolean active, final Boolean usequestcards) {
        this.name = name;
        this.project = project;
        this.active = active;
        this.usequestcards = usequestcards;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getProject() {
        return project;
    }

    public void setProject(final String project) {
        this.project = project;
    }

    public List<Quest> getQuests() {
        return quests;
    }

    public void setQuests(final List<Quest> quests) {
        this.quests = quests;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(final Boolean active) {
        this.active = active;
    }

    public Boolean getUsequestcards() {
        return usequestcards;
    }

    public void setUsequestcards(Boolean usequestcards) {
        this.usequestcards = usequestcards;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public List<Leaderboard> getLeaderboard() {
        if(leaderboard==null)
            leaderboard = new ArrayList<Leaderboard>();
        return leaderboard;
    }

    public void setLeaderboard(final List<Leaderboard> leaderboard) {
        this.leaderboard = leaderboard;
    }

    public String getImage() {
        return image;
    }

    public void setImage(final String image) {
        this.image = image;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(final List<User> users) {
        this.users = users;
    }

    public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public boolean hasQuests() {
        return !getQuests().isEmpty();
    }

    @Override
    public int hashCode() {
        return getId() == null ? super.hashCode() : Objects.hashCode(getId());
    }

    @Override
    public boolean equals(final Object that) {
        return getId() == null ? this == that
                : that != null && this.getClass().isInstance(that)
                        && Objects.equal(getId(), ((World) that).getId());
    }

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

}
