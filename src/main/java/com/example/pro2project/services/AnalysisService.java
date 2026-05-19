package com.example.pro2project.services;

import com.example.pro2project.models.*;
import com.example.pro2project.repositories.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AnalysisService {

    private final ParticipantRepository participantRepository;
    private final SummonerRepository summonerRepository;
    private final TagRepository tagRepository;
    private final AnalysisReportRepository analysisReportRepository;

    public AnalysisService(ParticipantRepository participantRepository,
                           SummonerRepository summonerRepository,
                           TagRepository tagRepository,
                           AnalysisReportRepository analysisReportRepository) {
        this.participantRepository = participantRepository;
        this.summonerRepository = summonerRepository;
        this.tagRepository = tagRepository;
        this.analysisReportRepository = analysisReportRepository;
    }

    @Transactional
    public void performAnalysis(String puuid) {
        Summoner summoner = summonerRepository.findByPuuid(puuid.trim());
        if (summoner == null) return;

        List<Participant> classicGames = participantRepository.findBySummoner(summoner).stream()
                .toList();

        if (classicGames.isEmpty()) return;

        AnalysisReport report = new AnalysisReport();
        report.setSummoner(summoner);

        report.setFarmingEfficiency(calculateFarmingEfficiency(classicGames));
        report.setAvgDamage(calculateAvgDamage(classicGames));
        report.setConsistencyScore(calculateConsistencyScore(classicGames));
        report.setKillParticipation(calculateKillParticipation(classicGames));
        report.setVisionScore(calculateVisionScore(classicGames));
        report.setWinRate(calculateWinRate(classicGames));

        analysisReportRepository.save(report);

        assignTags(summoner, report);
        summonerRepository.save(summoner);

        System.out.println(">>> Report uložen pro " + summoner.getName() + " (Počet her: " + classicGames.size() + ")");
    }

    private int calculateFarmingEfficiency(List<Participant> games) {
        double totalCsPerMin = 0;
        for (Participant p : games) {
            double minutes = p.getMatch().getGameDuration() / 60.0;
            if (minutes > 0) {
                totalCsPerMin += ((double) p.getTotalMinionsKilled() / minutes);
            }
        }
        return (int) Math.min(100, (totalCsPerMin / games.size()) * 10);
    }

    private int calculateAvgDamage(List<Participant> games) {
        long totalDamage = 0;
        for (Participant p : games) {
            totalDamage += p.getTotalDamage();
        }
        return (int) (totalDamage / games.size());
    }

    private int calculateConsistencyScore(List<Participant> games) {
        double totalKda = 0;
        for (Participant p : games) {
            totalKda += (double) (p.getKills() + p.getAssists()) / Math.max(1, p.getDeaths());
        }
        return (int) Math.min(100, (totalKda / games.size()) * 25);
    }

    private int calculateKillParticipation(List<Participant> games) {
        double totalKp = 0;
        for (Participant p : games) {
            List<Participant> allMatchParticipants = participantRepository.findByMatch(p.getMatch());
            int teamKills = allMatchParticipants.stream()
                    .filter(other -> other.getTeamId() == p.getTeamId())
                    .mapToInt(Participant::getKills)
                    .sum();

            if (teamKills > 0) {
                totalKp += (double) (p.getKills() + p.getAssists()) / teamKills;
            }
        }
        return (int) Math.min(100, (totalKp / games.size()) * 100);
    }

    private int calculateVisionScore(List<Participant> games) {
        int totalVision = 0;
        for (Participant p : games) {
            totalVision += p.getVisionScore();
        }
        return totalVision / games.size();
    }

    private int calculateWinRate(List<Participant> games) {
        int wins = 0;
        for (Participant p : games) {
            if (p.isWin()) wins++;
        }
        return (wins * 100) / games.size();
    }

    private void assignTags(Summoner summoner, AnalysisReport report) {
        if (summoner.getTags() == null) summoner.setTags(new ArrayList<>());
        summoner.getTags().clear();

        if (report.getConsistencyScore() > 75) addTag(summoner, "Carry");
        if (report.getConsistencyScore() < 35) addTag(summoner, "Feeder");
        if (report.getFarmingEfficiency() > 80) addTag(summoner, "Farmer");
        if (report.getVisionScore() > 40) addTag(summoner, "Vision King");
    }

    private void addTag(Summoner summoner, String tagName) {
        Tag tag = tagRepository.findByName(tagName);
        if (tag != null && !summoner.getTags().contains(tag)) {
            summoner.getTags().add(tag);
        }
    }
}