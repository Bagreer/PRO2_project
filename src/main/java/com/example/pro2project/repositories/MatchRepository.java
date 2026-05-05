package com.example.pro2project.repositories;

import com.example.pro2project.models.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
    Match findByMatchId(String matchId);
}
