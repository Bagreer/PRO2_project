package com.example.pro2project.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    // VAZBY (Relationships)

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role; // Každý uživatel má jednu roli (např. ROLE_USER)

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Note> notes; // Osobní poznámky uživatele k hráčům[cite: 4]

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<WatchList> watchedSummoners; // Seznam sledovaných vyvolávačů[cite: 4]

    // Konstruktory
    public User() {}
}
