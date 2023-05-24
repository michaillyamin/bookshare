package com.example.bookshare_project.services;

import com.example.bookshare_project.models.dto.UserDto;
import com.example.bookshare_project.models.entities.User;
import com.example.bookshare_project.models.enums.ERole;
import com.example.bookshare_project.models.exceptions.UserAlreadyExistsException;
import com.example.bookshare_project.models.payload.request.SignupRequest;
import com.example.bookshare_project.repositories.UserRepository;
import com.example.bookshare_project.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public User createUser(SignupRequest userIn) {
        User user = new User();
        user.setEmail(userIn.getEmail());
        user.setName(userIn.getFirstname());
        user.setLastname(userIn.getLastname());
        user.setUsername(userIn.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(userIn.getPassword()));
        user.getRole().add(ERole.ROLE_USER);

        try {
            log.info("Saving user {}", userIn.getEmail());
            return userRepository.save(user);
        } catch(Exception e) {
            log.error("Error during registration. {}", e.getMessage());
            throw new UserAlreadyExistsException("The user " + user.getUsername() + " already exists. Please check credentials");

        }
    }

    @Transactional
    public User updateUser(UserDto userDto, Principal principal) {
        User user = UserUtils.getUserByPrincipal(principal, userRepository);
        user.setName(userDto.getName());
        user.setUsername(userDto.getUsername());
        user.setLastname(userDto.getLastname());
        user.setBio(userDto.getBio());
        return userRepository.save(user);
    }

    public User getCurrentUser(Principal principal) {
        return UserUtils.getUserByPrincipal(principal, userRepository);
    }

    public User getUserById(Long userId) {
        return userRepository.findUserById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
