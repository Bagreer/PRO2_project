package com.example.pro2project.controllers;

import com.example.pro2project.models.AnalysisReport;
import com.example.pro2project.models.Participant;
import com.example.pro2project.models.Summoner;
import com.example.pro2project.repositories.ChampionRepository;
import com.example.pro2project.repositories.ParticipantRepository;
import com.example.pro2project.repositories.SummonerRepository;
import com.example.pro2project.repositories.AnalysisReportRepository;
import com.example.pro2project.services.AnalysisService;
import com.example.pro2project.services.RiotApiService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class WebController {

    private final SummonerRepository summonerRepository;
    private final AnalysisReportRepository analysisReportRepository;
    private final RiotApiService riotApiService;
    private final AnalysisService analysisService;
    private final ParticipantRepository participantRepository;

    public WebController(SummonerRepository summonerRepository,
                         AnalysisReportRepository analysisReportRepository,
                         RiotApiService riotApiService,
                         AnalysisService analysisService,
                         ChampionRepository championRepository,
                         ParticipantRepository participantRepository) {
        this.summonerRepository = summonerRepository;
        this.analysisReportRepository = analysisReportRepository;
        this.riotApiService = riotApiService;
        this.analysisService = analysisService;
//        this.championRepository = championRepository;
        this.participantRepository = participantRepository;
    }

    // Úvodní stránka s vyhledávacím polem
    @GetMapping("/")
    public String index() {
        return "index"; // Vytvoříš index.html v templates
    }

    @GetMapping("/search")
    public String searchPlayer(@RequestParam String name, @RequestParam String tag, Model model) {
        // 1. Zkusíme, jestli už jsme ho náhodou nehledali dřív (je v DB)
        Summoner summoner = summonerRepository.findByName(name);

        if (summoner == null) {
            try {
                // 2. Hráč v DB není -> jdeme pro něj do Riotu
                String puuid = riotApiService.getPuuid(name, tag);

                if (puuid != null) {
                    // 3. Stáhneme mu zápasy (např. posledních 10)
                    List<String> matchIds = riotApiService.getMatchIds(puuid);
                    for (String id : matchIds) {
                        riotApiService.downloadAndSaveMatch(id);
                    }

                    // 4. Spustíme tvou modularizovanou analýzu
                    analysisService.performAnalysis(puuid);

                    // 5. Teď už ho v DB máme, tak si ho vytáhneme
                    summoner = summonerRepository.findByPuuid(puuid);
                }
            } catch (Exception e) {
                model.addAttribute("error", "Chyba při komunikaci s Riot API. Zkontroluj jméno a tag.");
                return "index";
            }
        }

        // 6. Pokud summoner existuje (buď byl v DB, nebo jsme ho právě vytvořili), ukážeme profil
        if (summoner != null) {
            AnalysisReport report = analysisReportRepository.findBySummoner(summoner);

            // Vytáhneme historii her (všech módů)
            List<Participant> recentGames = participantRepository.findBySummonerOrderById(summoner);

            model.addAttribute("summoner", summoner);
            model.addAttribute("report", report); // Může být null, ošetříme v HTML
            model.addAttribute("tags", summoner.getTags());
            model.addAttribute("recentGames", recentGames); // Přidáno do modelu

            return "profile";
        }

        model.addAttribute("error", "Hráč nebyl nalezen.");
        return "index";
    }
}