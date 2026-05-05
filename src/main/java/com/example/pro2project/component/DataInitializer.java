package com.example.pro2project.component;

import com.example.pro2project.models.Role;
import com.example.pro2project.models.Tag;
import com.example.pro2project.models.User;
import com.example.pro2project.repositories.RoleRepository;
import com.example.pro2project.repositories.TagRepository;
import com.example.pro2project.repositories.UserRepository;
import com.example.pro2project.services.RiotApiService;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final RiotApiService riotApiService;

    public DataInitializer(RoleRepository roleRepository, TagRepository tagRepository, UserRepository userRepository, RiotApiService riotApiService) {
        this.roleRepository = roleRepository;
        this.tagRepository = tagRepository;
        this.userRepository = userRepository;
        this.riotApiService = riotApiService;
    }

    @Override
    @Transactional
    public void run(String... args) {
        // 1. DEFINICE ROLÍ
        if (roleRepository.count() == 0) {
            roleRepository.save(new Role("ROLE_ADMIN"));
            roleRepository.save(new Role("ROLE_USER"));
        }

        // 2. DEFINICE ANALYTICKÝCH TAGŮ (To, co bude tvá aplikace přiřazovat hráčům)
        if (tagRepository.count() == 0) {
            tagRepository.save(new Tag("Carry", "Hráč, který dává nejvíce dmg"));
            tagRepository.save(new Tag("Feeder", "Hráč s vysokým počtem úmrtí"));
            tagRepository.save(new Tag("Farmer", "Hráč s excelentním CS/min"));
            tagRepository.save(new Tag("Vision King", "Hráč s nejlepším vision score"));
        }

        // 3. PRVNÍ ADMIN ÚČET (Stále ho potřebuješ pro první login)
        if (userRepository.findByUsername("admin") == null) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword("heslo123"); // Později vyřešíme BCrypt šifrování
            admin.setRole(roleRepository.findByName("ROLE_ADMIN"));
            userRepository.save(admin);
        }

        System.out.println(">>> Master Data (Role, Tagy) byla úspěšně zinicializována.");
        System.out.println("Moje puuid: " + riotApiService.getPuuid("Bagreer","EUNE"));
        System.out.println("posledni moje hry: " + riotApiService.getMatchIds(riotApiService.getPuuid("Bagreer","EUNE")));

        List<String> matches = riotApiService.getMatchIds(riotApiService.getPuuid("Bagreer","EUNE"));

        for (String match : matches) {
            riotApiService.downloadAndSaveMatch(match);
        }
    }
}