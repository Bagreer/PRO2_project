package com.example.pro2project.repositories;

import com.example.pro2project.models.Note;
import com.example.pro2project.models.Summoner;
import com.example.pro2project.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    Note findByUserAndSummoner(User user, Summoner summoner);
    List<Note> findByUser(User user);
}
