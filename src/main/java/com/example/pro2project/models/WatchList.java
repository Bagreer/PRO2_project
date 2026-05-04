package com.example.pro2project.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "watchLists")
public class WatchList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // Který uživatel sleduje

    @ManyToOne
    @JoinColumn(name = "summoner_id")
    private Summoner summoner; // Kterého hráče sleduje

    public WatchList(int id) {
        this.id = id;
    }

    public WatchList() {

    }
}
