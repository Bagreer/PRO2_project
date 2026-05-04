package com.example.pro2project.models;

import jakarta.persistence.*;

@Entity
@Table(name = "summoners")
public class Summoner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String uuid;
    private String summonerLevel;


    public Summoner(Long id, String name, String uuid, String summonerLevel) {
        this.id = id;
        this.name = name;
        this.uuid = uuid;
        this.summonerLevel = summonerLevel;
    }

    public Summoner() {

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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getSummonerLevel() {
        return summonerLevel;
    }

    public void setSummonerLevel(String summonerLevel) {
        this.summonerLevel = summonerLevel;
    }
}
