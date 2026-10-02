package ru.library.libraryproject.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.library.libraryproject.entity.User;
import ru.library.libraryproject.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login)
            throws UsernameNotFoundException {

        User user = userRepository.findByLogin(login)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "Пользователь не найден: " + login
                        )
                );

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getLogin())
                .password(user.getPasswordHash())
                .roles(user.getRole().getName())
                .build();
    }
}