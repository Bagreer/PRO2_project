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

@SpringBootTest // Spustí celý testovací kontext aplikace i s databází
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
    void testUlozeniUzivatele() {
        // 1. Připravíme si testovacího uživatele
        User user = new User();
        user.setUsername("testovaciUzivatel");
        user.setPassword("bezpecneHeslo");

        // 2. Uložíme ho pomocí repozitáře do DB
        userRepository.save(user);

        // 3. Zkusíme ho podle jména z databáze vytáhnout zpět
        User nalezeny = userRepository.findByUsername("testovaciUzivatel");

        // 4. Klasické aserce (ověření, že data sedí)
        assertNotNull(nalezeny, "Uživatel by měl být v databázi nalezen.");
        assertEquals("bezpecneHeslo", nalezeny.getPassword(), "Heslo uloženého uživatele nesedí.");
    }

    @Test
    void testVytvoreniPoznamkyKHraci() {
        // 1. Připravíme uživatele a summonera
        User user = new User();
        user.setUsername("analytikMichal");
        user.setPassword("heslo");
        userRepository.save(user);

        Summoner summoner = new Summoner();
        summoner.setName("Faker");
        summoner.setPuuid("faker-puuid-123");
        summoner.setSummonerLevel(500);
        summonerRepository.save(summoner);

        // 2. Vytvoříme poznámku a propojíme ji s nimi
        Note note = new Note();
        note.setUser(user);
        note.setSummoner(summoner);
        note.setText("Pozor na jeho LeBlanc, hraje ji skvěle.");
        noteRepository.save(note);

        // 3. Ověříme přes repozitář, že se poznámka pro tuto dvojici uložila
        Note ulozenaPoznamka = noteRepository.findByUserAndSummoner(user, summoner);

        assertNotNull(ulozenaPoznamka);
        assertEquals("Pozor na jeho LeBlanc, hraje ji skvěle.", ulozenaPoznamka.getText());
    }

    void testVypoctuAnalizyAVytvoreniReportu() {
        // 1. PŘÍPRAVA DATA: Vytvoříme testovacího hráče
        Summoner summoner = new Summoner();
        summoner.setName("TestovaciHrac");
        summoner.setPuuid("puuid-klasik-123");
        summoner.setSummonerLevel(100);
        summoner.setTags(new ArrayList<>());
        summonerRepository.save(summoner);

        // 2. Vytvoříme zápas, který trval přesně 20 minut (1200 sekund)
        Match match = new Match();
        match.setMatchId("EUNE_TEST_1");
        match.setGameDuration(1200);
        match.setGameCreation(LocalDateTime.now());
        match.setGameMode("CLASSIC");
        matchRepository.save(match);

        // 3. Vytvoříme statistiky pro tohoto hráče v daném zápase
        Participant p = new Participant();
        p.setSummoner(summoner);
        p.setMatch(match);
        p.setKills(10);
        p.setDeaths(2);
        p.setAssists(5);
        p.setTotalDamage(25000);
        p.setTotalMinionsKilled(180);
        p.setVisionScore(20);
        p.setWin(true); // Nastavíme výhru
        p.setTeamId(100);
        participantRepository.save(p);

        // Pojistka: Vytvoříme v DB štítek "Carry", kdyby ho tvůj algoritmus vyžadoval
        if (tagRepository.findByName("Carry") == null) {
            Tag carryTag = new Tag();
            carryTag.setName("Carry");
            carryTag.setDescription("Vysoké KDA");
            tagRepository.save(carryTag);
        }

        // 4. AKCE: Spustíme tvou testovanou metodu z AnalysisService
        analysisService.performAnalysis("puuid-klasik-123");

        // 5. OVĚŘENÍ (Aserce): Vytáhneme si z DB report, který služba musela vygenerovat
        AnalysisReport report = analysisReportRepository.findBySummoner(summoner);

        // Ověříme, že report v databázi reálně existuje
        assertNotNull(report, "Analytický report by měl být úspěšně uložen do databáze.");

        // Ověříme matematické výpočty tvé služby
        assertEquals(100, report.getWinRate(), "Winrate by měl být 100%, protože vyhrál svůj jediný zápas.");
        assertEquals(25000, report.getAvgDamage(), "Průměrný damage nesedí s uloženým zápasem.");
        assertEquals(20, report.getVisionScore(), "Vision score nesedí.");
    }
}
