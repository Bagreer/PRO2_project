package com.example.pro2project.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "participants")
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Vazba na zápas
    @ManyToOne
    @JoinColumn(name = "match_id")
    private Match match;

    // Vazba na vyvolávače
    @ManyToOne
    @JoinColumn(name = "summoner_id")
    private Summoner summoner;

    // Vazba na šampiona
    @ManyToOne
    @JoinColumn(name = "champion_id")
    private Champion champion;

    private int kills;
    private int deaths;
    private int assists;
    private int totalDamage;
    private int totalMinionsKilled;

    public Participant(Long id, int kills, int deaths, int assists, int totalDamage) {
        this.id = id;
        this.kills = kills;
        this.deaths = deaths;
        this.assists = assists;
        this.totalDamage = totalDamage;
    }

    public Participant() {

    }
}
