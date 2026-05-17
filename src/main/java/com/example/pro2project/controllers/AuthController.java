package com.example.pro2project.controllers;

import com.example.pro2project.models.User;
import com.example.pro2project.models.Role;
import com.example.pro2project.repositories.UserRepository;
import com.example.pro2project.repositories.RoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String login() {
        return "login"; // Zobrazí login.html
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return "register"; // Zobrazí register.html
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username, @RequestParam String password, Model model) {
        if (userRepository.findByUsername(username) != null) {
            model.addAttribute("error", "Uživatelské jméno je již obsazené.");
            return "register";
        }

        User user = new User();
        user.setUsername(username);
        // ZAŠIFROVÁNÍ HESLA - kritické pro bezpečnost!
        user.setPassword(passwordEncoder.encode(password));

        // Přiřadíme výchozí roli USER (předpokládáme, že v DB už existuje)
        Role defaultRole = roleRepository.findByName("USER");
        if (defaultRole == null) {
            defaultRole = new Role();
            defaultRole.setName("USER");
            roleRepository.save(defaultRole);
        }
        user.setRole(defaultRole);

        userRepository.save(user);
        return "redirect:/login?success";
    }
}