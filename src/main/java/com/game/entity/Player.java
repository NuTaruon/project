package com.game.entity;
import jakarta.persistence.*;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@NamedQuery(name="Player.countAll",
        query = "SELECT count(p) FROM Player p")

@Table(name="player",schema="rpg")
public class Player {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(nullable = false)
    @JsonProperty("id")
    private Long id;

    @Column(name="name",nullable = false,length = 12)
    @JsonProperty("name")
    private String name;

    @Column(name="title",nullable = false,length = 30)
    @JsonProperty("title")
    private String title;

    @Column(name="race",nullable = false)
    @Enumerated(EnumType.ORDINAL)
    @JsonProperty("race")
    private Race race;

    @Column(name="profession",nullable = false)
    @Enumerated(EnumType.ORDINAL)
    @JsonProperty("profession")
    private Profession profession;

    @Column(name="birthday",nullable = false)
    @JsonProperty("birthday")
    private Date birthday;

    @Column(name="banned",nullable = false)
    @JsonProperty("banned")
    private Boolean banned;

    @Column(name="level",nullable = false)
    @JsonProperty("level")
    private Integer level;

    public Player() {
    }

    public Player(Long id, String name, String title, Race race, Profession profession, Date birthday, Boolean banned, Integer level) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.race = race;
        this.profession = profession;
        this.birthday = birthday;
        this.banned = banned;
        this.level = level;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public Profession getProfession() {
        return profession;
    }

    public void setProfession(Profession profession) {
        this.profession = profession;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Boolean getBanned() {
        return banned;
    }

    public void setBanned(Boolean banned) {
        this.banned = banned;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}