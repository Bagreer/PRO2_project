package com.example.pro2project.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
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

}
