package com.example.pro2project.repositories;

import com.example.pro2project.models.Participant;
import com.example.pro2project.models.Summoner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    List<Participant> findBySummoner(Summoner summoner);
}
