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

    @ManyToOne
    @JoinColumn(name = "match_id")
    private Match match;

    @ManyToOne
    @JoinColumn(name = "summoner_id")
    private Summoner summoner;

    @ManyToOne
    @JoinColumn(name = "champion_id")
    private Champion champion;

    private int kills;
    private int deaths;
    private int assists;
    private int totalDamage;
    private int teamId;
    private int totalMinionsKilled;
    private boolean win;
    private int visionScore;

    public Participant() {

    }
}
