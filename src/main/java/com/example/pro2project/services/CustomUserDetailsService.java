package com.example.pro2project.services;

import com.example.pro2project.models.User;
import com.example.pro2project.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Uživatel nenalezen: " + username);
        }

        // Vytáhneme tu jednu roli, kterou má uživatel přiřazenou
        String roleName = user.getRole() != null ? user.getRole().getName() : "USER";

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(roleName) // Spring Security automaticky přidá předponu "ROLE_"
                .build();
    }
}