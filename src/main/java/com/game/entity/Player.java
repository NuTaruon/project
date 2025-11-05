package com.game.entity;

import jakarta.persistence.*;
import org.springframework.lang.NonNull;

import java.util.Date;



@Entity
@NamedQuery(name = "Player.GetAllCount", query = "select count(p) from Player p")
@Table(name = "player", schema = "rpg")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",  nullable = false)
    private Long id;

    @NonNull
    @Column(name = "name", nullable = false, length = 12)
    private String name;

    @NonNull
    @Column(name = "title", nullable = false, length = 30)
    private String title;

    @NonNull
    @Column(name = "race",  nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private Race race;

    @NonNull
    @Column(name = "profession", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private Profession profession;

    @NonNull
    @Column(name = "birthday", nullable = false)
    private Date birthday;

    @NonNull
    @Column(name = "banned", nullable = false)
    private Boolean banned;

    @NonNull
    @Column(name = "level", nullable = false)
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