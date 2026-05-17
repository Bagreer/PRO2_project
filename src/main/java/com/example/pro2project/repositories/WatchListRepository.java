package com.example.pro2project.repositories;

import com.example.pro2project.models.Summoner;
import com.example.pro2project.models.User;
import com.example.pro2project.models.WatchList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WatchListRepository extends JpaRepository<WatchList, Long> {
    List<WatchList> findByUser(User user);
    boolean existsByUserAndSummoner(User user, Summoner summoner);
    void deleteByUserAndSummoner(User user, Summoner summoner);
}
