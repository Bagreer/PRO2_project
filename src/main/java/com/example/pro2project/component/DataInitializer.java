package com.example.pro2project.component;

import com.example.pro2project.models.Role;
import com.example.pro2project.models.Summoner;
import com.example.pro2project.models.Tag;
import com.example.pro2project.models.User;
import com.example.pro2project.repositories.RoleRepository;
import com.example.pro2project.repositories.SummonerRepository;
import com.example.pro2project.repositories.TagRepository;
import com.example.pro2project.repositories.UserRepository;
import com.example.pro2project.services.AnalysisService;
import com.example.pro2project.services.RiotApiService;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final RiotApiService riotApiService;
    private final AnalysisService analysisService;
    private final SummonerRepository summonerRepository;

    public DataInitializer(RoleRepository roleRepository,
                           TagRepository tagRepository,
                           UserRepository userRepository,
                           RiotApiService riotApiService,
                           AnalysisService analysisService,
                           SummonerRepository summonerRepository) {
        this.roleRepository = roleRepository;
        this.tagRepository = tagRepository;
        this.userRepository = userRepository;
        this.riotApiService = riotApiService;
        this.analysisService = analysisService;
        this.summonerRepository = summonerRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        // Nahraj jen číselníky
        if (roleRepository.count() == 0) {
            roleRepository.save(new Role("ROLE_USER"));
            roleRepository.save(new Role("ROLE_ADMIN"));
        }

        if (tagRepository.count() == 0) {
            tagRepository.save(new Tag("Carry", "Hráč s vysokým poškozením"));
            tagRepository.save(new Tag("Feeder", "Hráč s vysokým počtem úmrtí"));
            tagRepository.save(new Tag("Vision King", "Hráč s výbornou vizí"));
            // ... další tagy
        }

        riotApiService.getChampFromDragon();

        System.out.println(">>> Backend připraven, čekám na vyhledávání...");
    }

//        String myPuuid = riotApiService.getPuuid("Bagreer", "EUNE");
//        System.out.println(">>> Master Data (Role, Tagy) byla úspěšně zinicializována.");
//        System.out.println("Moje puuid: " + myPuuid);
//        System.out.println("posledni moje hry: " + riotApiService.getMatchIds(myPuuid));
//
//        List<String> matches = riotApiService.getMatchIds(myPuuid);
//
//        for (String match : matches) {
//            riotApiService.downloadAndSaveMatch(match);
//        }
//
//        List<Summoner> allSummoners = summonerRepository.findAll();
//
//        for (Summoner s : allSummoners) {
//            if (s.getPuuid() != null) {
//                analysisService.performAnalysis(s.getPuuid());
//            }
//        }
}