package ru.library.libraryproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth

                        // Доступ без авторизации
                        .requestMatchers(
                                "/login",
                                "/register",
                                "/style.css",
                                "/library-background.png"
                        ).permitAll()

                        // Книги доступны авторизованным пользователям
                        .requestMatchers("/books")
                        .authenticated()

                        // Раздел читателя
                        .requestMatchers("/reader/**")
                        .hasRole("READER")

                        // Раздел библиотекаря
                        .requestMatchers("/librarian/**")
                        .hasRole("LIBRARIAN")

                        // Раздел администратора
                        .requestMatchers("/admin/**")
                        .hasRole("ADMIN")

                        // Остальные страницы требуют авторизации
                        .anyRequest()
                        .authenticated()
                )

                .formLogin(form -> form

                        .loginPage("/login")

                        // Перенаправление в зависимости от роли
                        .successHandler((request, response, authentication) -> {

                            String role = authentication.getAuthorities()
                                    .stream()
                                    .findFirst()
                                    .map(authority -> authority.getAuthority())
                                    .orElse("");

                            if (role.equals("ROLE_READER")) {
                                response.sendRedirect("/reader");

                            } else if (role.equals("ROLE_LIBRARIAN")) {
                                response.sendRedirect("/librarian");

                            } else if (role.equals("ROLE_ADMIN")) {
                                response.sendRedirect("/admin");

                            } else {
                                response.sendRedirect("/books");
                            }
                        })

                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }
}