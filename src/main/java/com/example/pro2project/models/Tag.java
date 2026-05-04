package com.example.pro2project.models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "summoners")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToMany
    @JoinTable(name = "summoner_tags")
    private List<Summoner> summoners;

    public Tag(int id) {
        this.id = id;
    }

    public Tag() {

    }
}
