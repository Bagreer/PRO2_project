package com.example.pro2project.models;

import jakarta.persistence.*;

@Entity
@Table(name = "participants")
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int kills;
    private int deaths;
    private int assists;
    private int totalDamage;
}
