package com.example.pro2project.models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "summoners")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    public Tag(int id) {
        this.id = id;
    }

    public Tag() {

    }
}
