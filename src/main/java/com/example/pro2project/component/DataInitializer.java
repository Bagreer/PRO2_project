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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final RiotApiService riotApiService;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RoleRepository roleRepository,
                           TagRepository tagRepository,
                           UserRepository userRepository,
                           RiotApiService riotApiService,
                           PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.tagRepository = tagRepository;
        this.userRepository = userRepository;
        this.riotApiService = riotApiService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (roleRepository.count() == 0) {
            roleRepository.save(new Role("ROLE_USER"));
            roleRepository.save(new Role("ROLE_ADMIN"));
        }

        if (tagRepository.count() == 0) {
            tagRepository.save(new Tag("Carry", "Hráč s vysokým poškozením"));
            tagRepository.save(new Tag("Feeder", "Hráč s vysokým počtem úmrtí"));
            tagRepository.save(new Tag("Vision King", "Hráč s výbornou vizí"));
        }

        riotApiService.getChampFromDragon();

        if (userRepository.findByUsername("admin") == null) {
            User admin = new User();
            admin.setUsername("admin");
            // Heslo musíme zašifrovat přes bcrypt, jinak by tě Spring Security nepustil dál
            admin.setPassword(passwordEncoder.encode("admin"));

            userRepository.save(admin);
            System.out.println(">>>> 👤 Výchozí uživatel 'admin' s heslem 'admin' byl úspěšně vytvořen.");
        }

        System.out.println(">>> Backend připraven, čekám na vyhledávání...");


    }
}