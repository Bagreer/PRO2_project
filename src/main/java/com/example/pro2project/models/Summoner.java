package com.example.pro2project.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "summoners")
public class Summoner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String puuid; // Unikátní ID z Riot API pro identifikaci hráče[cite: 3, 10]

    private String name; // Herní jméno vyvolávače

    private int summonerLevel;
    private String iconId;

    // VAZBY (Relationships)

    @OneToMany(mappedBy = "summoner", cascade = CascadeType.ALL)
    private List<Participant> matchHistory; // Výkony v konkrétních zápasech[cite: 4]

    @OneToMany(mappedBy = "summoner", cascade = CascadeType.ALL)
    private List<AnalysisReport> reports; // Tvé vypočítané hloubkové analýzy

    @OneToMany(mappedBy = "summoner")
    private List<WatchList> followedByUsers; // Kteří uživatelé si tohoto hráče přidali[cite: 4]

    @ManyToMany
    @JoinTable(
            name = "summoner_tags_map",
            joinColumns = @JoinColumn(name = "summoner_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )

    private List<Tag> tags = new java.util.ArrayList<>();

    // Konstruktory
    public Summoner() {}

}
