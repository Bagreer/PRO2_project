package com.example.pro2project.repositories;

import com.example.pro2project.models.Summoner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SummonerRepository extends JpaRepository<Summoner, Long> {
    Summoner findByName(String name); // Pro hledání hráče z Riot API
    Summoner findByPuuid(String puuid);
}
