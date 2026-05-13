package com.example.pro2project.repositories;

import com.example.pro2project.models.AnalysisReport;
import com.example.pro2project.models.Summoner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnalysisReportRepository extends JpaRepository<AnalysisReport, Long> {

    AnalysisReport findBySummoner(Summoner summoner);
}
