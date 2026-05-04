package com.example.pro2project.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "champions")
public class Champion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToMany(mappedBy = "champion")
    private List<Participant> usages;

    private int championId;
    private String name;
    private String championClass;
    private String description;
}
