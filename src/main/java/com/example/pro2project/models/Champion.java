package com.example.pro2project.models;

import jakarta.persistence.*;

@Entity
@Table(name = "champions")
public class Champion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int championId;
    private String name;
    private String championClass;
    private String description;
}
