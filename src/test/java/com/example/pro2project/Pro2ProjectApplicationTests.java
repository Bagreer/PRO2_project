package com.example.pro2project;

import com.example.pro2project.models.*;
import com.example.pro2project.repositories.*;
import com.example.pro2project.services.AnalysisService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class Pro2ProjectApplicationTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SummonerRepository summonerRepository;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private AnalysisReportRepository analysisReportRepository;

    @Autowired
    private AnalysisService analysisService;

    @Test
    void contextLoads() {

    }

    @Test
    void testUserSave() {
        User user = new User();
        user.setUsername("testovaciUzivatel");
        user.setPassword("bezpecneHeslo");

        userRepository.save(user);

        User nalezeny = userRepository.findByUsername("testovaciUzivatel");

        assertNotNull(nalezeny, "Uživatel by měl být v databázi nalezen.");
        assertEquals("bezpecneHeslo", nalezeny.getPassword(), "Heslo uloženého uživatele nesedí.");
    }

    @Test
    void testNoteCreation() {
        User user = new User();
        user.setUsername("analytikMichal");
        user.setPassword("heslo");
        userRepository.save(user);

        Summoner summoner = new Summoner();
        summoner.setName("Faker");
        summoner.setPuuid("faker-puuid-123");
        summoner.setSummonerLevel(500);
        summonerRepository.save(summoner);

        Note note = new Note();
        note.setUser(user);
        note.setSummoner(summoner);
        note.setText("Pozor na jeho LeBlanc, hraje ji skvěle.");
        noteRepository.save(note);

        Note ulozenaPoznamka = noteRepository.findByUserAndSummoner(user, summoner);

        assertNotNull(ulozenaPoznamka);
        assertEquals("Pozor na jeho LeBlanc, hraje ji skvěle.", ulozenaPoznamka.getText());
    }

    void testAnalysisReportCalculation() {
        Summoner summoner = new Summoner();
        summoner.setName("TestovaciHrac");
        summoner.setPuuid("puuid-klasik-123");
        summoner.setSummonerLevel(100);
        summoner.setTags(new ArrayList<>());
        summonerRepository.save(summoner);

        Match match = new Match();
        match.setMatchId("EUNE_TEST_1");
        match.setGameDuration(1200);
        match.setGameCreation(LocalDateTime.now());
        match.setGameMode("CLASSIC");
        matchRepository.save(match);

        Participant p = new Participant();
        p.setSummoner(summoner);
        p.setMatch(match);
        p.setKills(10);
        p.setDeaths(2);
        p.setAssists(5);
        p.setTotalDamage(25000);
        p.setTotalMinionsKilled(180);
        p.setVisionScore(20);
        p.setWin(true);
        p.setTeamId(100);
        participantRepository.save(p);

        if (tagRepository.findByName("Carry") == null) {
            Tag carryTag = new Tag();
            carryTag.setName("Carry");
            carryTag.setDescription("Vysoké KDA");
            tagRepository.save(carryTag);
        }

        analysisService.performAnalysis("puuid-klasik-123");

        AnalysisReport report = analysisReportRepository.findBySummoner(summoner);

        assertNotNull(report, "Analytický report by měl být úspěšně uložen do databáze.");

        assertEquals(100, report.getWinRate(), "Winrate by měl být 100%, protože vyhrál svůj jediný zápas.");
        assertEquals(25000, report.getAvgDamage(), "Průměrný damage nesedí s uloženým zápasem.");
        assertEquals(20, report.getVisionScore(), "Vision score nesedí.");
    }
}