package com.example.pro2project.services;

import com.example.pro2project.models.Match;
import com.example.pro2project.models.Participant;
import com.example.pro2project.repositories.MatchRepository;
import com.example.pro2project.repositories.ParticipantRepository;
import dto.MatchDetailDTO;
import dto.RiotAccountDTO;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class RiotApiService {

    @Value("${riot.api.key}")
    private String apiKey;

    private final MatchRepository matchRepository;
    private final ParticipantRepository participantRepository;

    private final RestTemplate restTemplate;

    public RiotApiService(RestTemplate restTemplate,
                          MatchRepository matchRepository,
                          ParticipantRepository participantRepository) {
        this.restTemplate = restTemplate;
        this.matchRepository = matchRepository;
        this.participantRepository = participantRepository;
    }

    public String getPuuid(String gameName, String tagLine) {
        // Pozor: Riot teď vyžaduje volání přes /account/v1/accounts/by-riot-id/
        String url = "https://europe.api.riotgames.com/riot/account/v1/accounts/by-riot-id/"
                + gameName + "/" + tagLine + "?api_key=" + apiKey;

        try {
            RiotAccountDTO account = restTemplate.getForObject(url, RiotAccountDTO.class);
            return account != null ? account.getPuuid() : null;
        } catch (Exception e) {
            System.err.println("Chyba při volání Riot API: " + e.getMessage());
            return null;
        }
    }

    public List<String> getMatchIds(String puuid) {
        int count = 20;
        String url = "https://europe.api.riotgames.com/lol/match/v5/matches/by-puuid/" +
                puuid + "/ids?start=0&count=" +
                count + "&api_key=" + apiKey;

        try {
            String[] matchIds = restTemplate.getForObject(url, String[].class);
            return matchIds != null ? Arrays.asList(matchIds) : Collections.emptyList();
        } catch (Exception e) {
            System.err.println("chyba při získávání Match IDs: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    @Transactional
    public void downloadAndSaveMatch(String matchId) {
        // 1. Kontrola, zda už zápas v DB nemáme
        if (matchRepository.findByMatchId(matchId) != null) {
            return;
        }

        String url = "https://europe.api.riotgames.com/lol/match/v5/matches/" + matchId + "?api_key=" + apiKey;

        try {
            MatchDetailDTO dto = restTemplate.getForObject(url, MatchDetailDTO.class);
            if (dto != null) { // Chceme jen klasiky, ne ARAMy

                // 2. Vytvoření a uložení entity Match
                Match match = new Match();
                match.setMatchId(matchId);
                match.setGameDuration((int) dto.getInfo().getGameDuration());
                match = matchRepository.save(match); // Uložíme a získáme match s vygenerovaným ID

                // 3. Uložení účastníků
                for (MatchDetailDTO.ParticipantDTO pDto : dto.getInfo().getParticipants()) {
                    Participant p = new Participant();
                    p.setKills(pDto.getKills());
                    p.setDeaths(pDto.getDeaths());
                    p.setAssists(pDto.getAssists());
                    p.setTotalDamage(pDto.getTotalDamageDealtToChampions());
                    p.setMatch(match); // Propojení s právě vytvořeným zápasem

                    participantRepository.save(p);
                }
                System.out.println("Zápas " + matchId + " byl úspěšně uložen do DB.");
            }
        } catch (Exception e) {
            System.err.println("Chyba při stahování zápasu " + matchId + ": " + e.getMessage());
        }
    }
}