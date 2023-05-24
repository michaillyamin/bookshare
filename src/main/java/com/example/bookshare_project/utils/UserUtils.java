package com.example.bookshare_project.utils;

import com.example.bookshare_project.models.entities.User;
import com.example.bookshare_project.repositories.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.security.Principal;

public class UserUtils {

    public static User getUserByPrincipal(Principal principal, UserRepository userRepository) {
        String username = principal.getName();
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with %s not found".formatted(username)));
    }

}
