package com.example.pro2project.services;

import com.example.pro2project.models.Champion;
import com.example.pro2project.models.Match;
import com.example.pro2project.models.Participant;
import com.example.pro2project.models.Summoner;
import com.example.pro2project.repositories.ChampionRepository;
import com.example.pro2project.repositories.MatchRepository;
import com.example.pro2project.repositories.ParticipantRepository;
import com.example.pro2project.dto.MatchDetailDTO;
import com.example.pro2project.dto.RiotAccountDTO;
import com.example.pro2project.repositories.SummonerRepository;
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
    private final SummonerRepository summonerRepository;
    private final ChampionRepository championRepository;

    private final RestTemplate restTemplate;

    public RiotApiService(RestTemplate restTemplate,
                          MatchRepository matchRepository,
                          ParticipantRepository participantRepository,
                          SummonerRepository summonerRepository,
                          ChampionRepository championRepository) {
        this.restTemplate = restTemplate;
        this.matchRepository = matchRepository;
        this.participantRepository = participantRepository;
        this.summonerRepository = summonerRepository;
        this.championRepository = championRepository;
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
        if (matchRepository.findByMatchId(matchId) != null) return;

        String url = "https://europe.api.riotgames.com/lol/match/v5/matches/" + matchId + "?api_key=" + apiKey;
        System.out.println("Using URL: " + url);

        try {
            MatchDetailDTO dto = restTemplate.getForObject(url, MatchDetailDTO.class);
            if (dto != null && "CLASSIC".equals(dto.getInfo().getGameMode())) {

                Match match = new Match();
                match.setMatchId(matchId);
                match.setGameDuration((int) dto.getInfo().getGameDuration());
                match = matchRepository.save(match);

                for (MatchDetailDTO.ParticipantDTO pDto : dto.getInfo().getParticipants()) {
                    // 1. Zpracování vyvolávače (Summoner)
                    Summoner summoner = summonerRepository.findByPuuid(pDto.getPuuid());
                    if (summoner == null) {
                        summoner = new Summoner();
                        summoner.setPuuid(pDto.getPuuid());
                        summoner.setName(pDto.getSummonerName());
                        summoner.setSummonerLevel((pDto.getSummonerLevel()));
                        // Level můžeme nastavit později nebo teď, pokud je v DTO
                        summoner = summonerRepository.save(summoner);
                    }

                    // 2. Zpracování šampiona (Champion)
                    // Pro zjednodušení teď hledáme/vytváříme jen podle ID
                    Champion champion = championRepository.findById((long) pDto.getChampionId()).orElse(null);
                    if (champion == null) {
                        champion = new Champion();
                        champion.setChampionId(pDto.getChampionId());
                        // Jméno šampiona bychom museli dotáhnout z Data Dragonu, teď necháme ID
                        champion = championRepository.save(champion);
                    }

                    // 3. Vytvoření účastníka se všemi vazbami
                    Participant p = new Participant();
                    p.setKills(pDto.getKills());
                    p.setDeaths(pDto.getDeaths());
                    p.setAssists(pDto.getAssists());
                    p.setTotalDamage(pDto.getTotalDamageDealtToChampions());

                    p.setMatch(match);       // Vazba na Match
                    p.setSummoner(summoner); // Vazba na Summoner
                    p.setChampion(champion); // Vazba na Champion

                    participantRepository.save(p);
                }
                System.out.println("Zápas " + matchId + " kompletně uložen (včetně vazeb).");
            }
        } catch (Exception e) {
            System.err.println("Chyba při stahování zápasu " + matchId + ": " + e.getMessage());
        }
    }
}