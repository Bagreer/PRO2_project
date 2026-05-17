package com.example.pro2project.repositories;

import com.example.pro2project.models.Note;
import com.example.pro2project.models.Summoner;
import com.example.pro2project.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    Note findByUserAndSummoner(User user, Summoner summoner);
}
