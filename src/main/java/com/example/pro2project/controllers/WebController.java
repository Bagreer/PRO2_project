package com.example.pro2project.controllers;

import com.example.pro2project.models.*;
import com.example.pro2project.repositories.*;
import com.example.pro2project.services.AnalysisService;
import com.example.pro2project.services.RiotApiService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
public class WebController {

    private final SummonerRepository summonerRepository;
    private final AnalysisReportRepository analysisReportRepository;
    private final RiotApiService riotApiService;
    private final AnalysisService analysisService;
    private final ParticipantRepository participantRepository;
    private final ChampionRepository championRepository;
    private final UserRepository userRepository;
    private final WatchListRepository watchListRepository;
    private final NoteRepository noteRepository;

    public WebController(SummonerRepository summonerRepository,
                         AnalysisReportRepository analysisReportRepository,
                         RiotApiService riotApiService,
                         AnalysisService analysisService,
                         ChampionRepository championRepository,
                         ParticipantRepository participantRepository,
                         UserRepository userRepository,
                         WatchListRepository watchListRepository,
                         NoteRepository noteRepository) {
        this.summonerRepository = summonerRepository;
        this.analysisReportRepository = analysisReportRepository;
        this.riotApiService = riotApiService;
        this.analysisService = analysisService;
        this.championRepository = championRepository;
        this.participantRepository = participantRepository;
        this.userRepository = userRepository;
        this.watchListRepository = watchListRepository;
        this.noteRepository = noteRepository;
    }

    // Úvodní stránka s vyhledávacím polem
    @GetMapping("/")
    public String index() {
        return "index"; // Vytvoříš index.html v templates
    }

    @GetMapping("/search")
    public String searchPlayer(@RequestParam String name, @RequestParam String tag, Model model, Principal principal) {
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

        if (summoner != null) {
            AnalysisReport report = analysisReportRepository.findBySummoner(summoner);

            User user = userRepository.findByUsername(principal.getName());
            Note existingNote = noteRepository.findByUserAndSummoner(user, summoner);
            model.addAttribute("note", existingNote != null ? existingNote.getText() : "");

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

    @PostMapping("/watchlist/add")
    public String addToWatchList(@RequestParam Long summonerId, Principal principal) {
        User user = userRepository.findByUsername(principal.getName());
        Summoner summoner = summonerRepository.findById(summonerId).orElse(null);

        if (user != null && summoner != null && !watchListRepository.existsByUserAndSummoner(user, summoner)) {
            WatchList wl = new WatchList();
            wl.setUser(user);
            wl.setSummoner(summoner);
            watchListRepository.save(wl);
        }
        return "redirect:/search?name=" + summoner.getName() + "&tag=EUNE"; // vrátí tě to zpět na profil (případně uprav podle tvého tagu)
    }

    @PostMapping("/notes/save")
    public String saveNote(@RequestParam Long summonerId, @RequestParam String text, Principal principal) {
        User user = userRepository.findByUsername(principal.getName());
        Summoner summoner = summonerRepository.findById(summonerId).orElse(null);

        if (user != null && summoner != null) {
            Note note = noteRepository.findByUserAndSummoner(user, summoner);
            if (note == null) {
                note = new Note();
                note.setUser(user);
                note.setSummoner(summoner);
            }
            note.setText(text);
            System.out.println("NOTE SAVED");
            noteRepository.save(note);
        }
        // Vrátí tě to zpět na profil toho stejného hráče
        return "redirect:/search?name=" + (summoner != null ? summoner.getName() : "") + "&tag=EUNE";
    }
}