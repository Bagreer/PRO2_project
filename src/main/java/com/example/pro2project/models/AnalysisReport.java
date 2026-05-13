package com.example.pro2project.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "AnalysisReports")
public class AnalysisReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int consistencyScore;
    private int FarmingEfficiency;
    private int killParticipation;
    private int avgDamage;
    private int winRate;

    @ManyToOne
    @JoinColumn(name = "summoner_id")
    private Summoner summoner;

    public AnalysisReport(Long id, int consistencyScore, int farmingEfficiency) {
        this.id = id;
        this.consistencyScore = consistencyScore;
        FarmingEfficiency = farmingEfficiency;
    }

    public AnalysisReport() {

    }
}
