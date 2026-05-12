package com.example.pro2project.services;

import com.example.pro2project.dto.ChampionDataDTO;
import com.example.pro2project.dto.ChampionListDTO;
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
        System.out.println("using URL: " + url);

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
            if (dto != null) {

                Match match = new Match();
                match.setMatchId(matchId);
                match.setGameDuration((int) dto.getInfo().getGameDuration());

                String mode = dto.getInfo().getGameMode();
                if ("CHERRY".equalsIgnoreCase(mode)) {
                    match.setGameMode("ARENA");
                } else {
                    match.setGameMode(mode);
                }
                match = matchRepository.save(match);

                for (MatchDetailDTO.ParticipantDTO pDto : dto.getInfo().getParticipants()) {
                    // 1. Zpracování vyvolávače (Summoner)
                    Summoner summoner = summonerRepository.findByPuuid(pDto.getPuuid());
                    if (summoner == null) {
                        summoner = new Summoner();
                        summoner.setPuuid(pDto.getPuuid());
                    }

                    if (pDto.getRiotIdGameName() != null && !pDto.getRiotIdGameName().isEmpty()) {
                        summoner.setName(pDto.getRiotIdGameName());
                    } else {
                        summoner.setName("Unknown");
                    }

                    summoner.setSummonerLevel(pDto.getSummonerLevel());
                    summoner = summonerRepository.save(summoner);

                    // 2. Zpracování šampiona (Champion)
                    // Pro zjednodušení teď hledáme/vytváříme jen podle ID
                    Champion champion = championRepository.findByChampionId(pDto.getChampionId());
                    if (champion == null) {
                        champion = new Champion();
                        champion.setChampionId(pDto.getChampionId());
                        champion.setName("ID: " + pDto.getChampionId()); // Dočasné jméno
                        champion = championRepository.save(champion);
                    }

                    Participant p = new Participant();
                    p.setKills(pDto.getKills());
                    p.setDeaths(pDto.getDeaths());
                    p.setAssists(pDto.getAssists());
                    p.setTotalDamage(pDto.getTotalDamageDealtToChampions());

                    p.setMatch(match);
                    p.setSummoner(summoner); // Tady zmizí ten NULL v PARTICIPANTS
                    p.setChampion(champion); // Tady zmizí ten NULL v PARTICIPANTS

                    participantRepository.save(p);
                }
                System.out.println("Zápas " + matchId + " kompletně uložen (včetně vazeb).");
            }
        } catch (Exception e) {
            System.err.println("Chyba při stahování zápasu " + matchId + ": " + e.getMessage());
        }
    }


    @Transactional
    public void getChampFromDragon() {
        // URL pro verzi 14.9.1 (Riot verze občas mění, ale tato je teď stabilní)
        String url = "https://ddragon.leagueoflegends.com/cdn/14.9.1/data/en_US/champion.json";
        System.out.println("Using URL: " + url);

        try {
            ChampionListDTO response = restTemplate.getForObject(url, ChampionListDTO.class);
            if (response != null && response.getData() != null) {
                for (ChampionDataDTO dto : response.getData().values()) {
                    int riotId = Integer.parseInt(dto.getKey());

                    // Zkusíme, jestli už šampiona máme
                    Champion champion = championRepository.findByChampionId(riotId);
                    if (champion == null) {
                        champion = new Champion();
                        champion.setChampionId(riotId);
                    }

                    champion.setName(dto.getId()); // "Aatrox"
                    champion.setDescription(dto.getTitle()); // "the Darkin Blade"

                    // Vezmeme první tag jako hlavní classu (např. "Fighter")
                    if (!dto.getTags().isEmpty()) {
                        champion.setChampionClass(dto.getTags().get(0));
                    }

                    championRepository.save(champion);
                }
                System.out.println(">>> Všech " + response.getData().size() + " šampionů z Data Dragonu nahráno.");
            }
        } catch (Exception e) {
            System.err.println("Chyba při stahování šampionů: " + e.getMessage());
        }
    }
}