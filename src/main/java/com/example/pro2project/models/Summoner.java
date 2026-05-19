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
    private String puuid;
    private String name;
    private int summonerLevel;
    private String iconId;


    @OneToMany(mappedBy = "summoner", cascade = CascadeType.ALL)
    private List<Participant> matchHistory;

    @OneToMany(mappedBy = "summoner", cascade = CascadeType.ALL)
    private List<AnalysisReport> reports;

    @OneToMany(mappedBy = "summoner")
    private List<WatchList> followedByUsers;

    @ManyToMany
    @JoinTable(
            name = "summoner_tags_map",
            joinColumns = @JoinColumn(name = "summoner_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )

    private List<Tag> tags = new java.util.ArrayList<>();

    public Summoner() {}

}
