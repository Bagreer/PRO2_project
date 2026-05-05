package com.example.pro2project.models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "tags")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String description;

    @ManyToMany
    @JoinTable(name = "summoner_tags")
    private List<Summoner> summoners;

    public Tag(int id, String name, String description, List<Summoner> summoners) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.summoners = summoners;
    }

    public Tag(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Tag() {

    }
}
