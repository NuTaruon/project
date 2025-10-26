package com.game.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "player", schema = "rpg")
@NamedQuery(name = "Player.getAllCount", query = "SELECT COUNT(p) FROM Player p")
public class Player {
    private Date birthdate;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 12, nullable = false)
    private String name;

    @Column(name = "title", length = 30, nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "race")
    private Race race;

    @Enumerated(EnumType.STRING)
    @Column(name = "profession")
    private Profession profession;

    @Column(name = "experience", nullable = false)
    private Integer experience;

    @Column(name = "level", nullable = false)
    private Integer level;

    @Column(name = "untilNextLevel", nullable = false)
    private Integer untilNextLevel;

    @Column(name = "banned", nullable = false)
    private Boolean banned;

    // Constructors
    public Player() {
    }

    public Player(String name, String title, Race race, Profession profession,
                  Integer experience, Integer level, Integer untilNextLevel,
                  Boolean banned) {
        this.name = name;
        this.title = title;
        this.race = race;
        this.profession = profession;
        this.experience = experience;
        this.level = level;
        this.untilNextLevel = untilNextLevel;
        this.banned = banned;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Race getRace() { return race; }
    public void setRace(Race race) { this.race = race; }

    public Profession getProfession() { return profession; }
    public void setProfession(Profession profession) { this.profession = profession; }

    public Integer getExperience() { return experience; }
    public void setExperience(Integer experience) { this.experience = experience; }

    public Integer getLevel() { return level; }
    public void setLevel(Integer level) { this.level = level; }

    public Integer getUntilNextLevel() { return untilNextLevel; }
    public void setUntilNextLevel(Integer untilNextLevel) { this.untilNextLevel = untilNextLevel; }

    public Boolean getBanned() { return banned; }
    public void setBanned(Boolean banned) { this.banned = banned; }
    public Date getBirthdate() {
        return birthdate;
    }
    public void setBirthdate(Date date) {
        this.birthdate = date;
}
}