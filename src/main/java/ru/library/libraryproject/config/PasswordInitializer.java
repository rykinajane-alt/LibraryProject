package ru.library.libraryproject.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.library.libraryproject.entity.User;
import ru.library.libraryproject.repository.UserRepository;

@Configuration
public class PasswordInitializer {

    @Bean
    public CommandLineRunner initializePasswords(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            for (User user : userRepository.findAll()) {

                if (user.getPasswordHash().startsWith("TEMP_HASH_")) {
                    user.setPasswordHash(
                            passwordEncoder.encode("library123")
                    );

                    userRepository.save(user);
                }
            }

            // Тестовый пароль библиотекаря
            User librarian = userRepository.findByLogin("anna")
                    .orElse(null);

            if (librarian != null) {
                librarian.setPasswordHash(
                        passwordEncoder.encode("12345")
                );

                userRepository.save(librarian);
            }
        };
    }
}