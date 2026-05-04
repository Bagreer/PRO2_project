package com.example.pro2project.models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "matches")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String matchId;
    private int gameDuration;
    private int gameMode;

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL)
    private List<Participant> participants; // 10 účastníků zápasu

    public Match(Long id, String matchId, int gameDuration, int gameMode) {
        this.id = id;
        this.matchId = matchId;
        this.gameDuration = gameDuration;
        this.gameMode = gameMode;
    }

    public Match() {

    }

}
