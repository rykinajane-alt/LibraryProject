
package ru.library.libraryproject.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/login",
                                "/register",
                                "/style.css",
                                "/library-background.png",
                                "/images/**"
                        ).permitAll()

                        .requestMatchers("/reader/**").hasRole("READER")
                        .requestMatchers("/librarian/**").hasRole("LIBRARIAN")
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        .requestMatchers("/books").authenticated()
                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .successHandler((request, response, authentication) -> {
                            boolean isAdmin = authentication.getAuthorities()
                                    .stream()
                                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

                            boolean isLibrarian = authentication.getAuthorities()
                                    .stream()
                                    .anyMatch(a -> a.getAuthority().equals("ROLE_LIBRARIAN"));

                            if (isAdmin) {
                                response.sendRedirect("/admin");
                            } else if (isLibrarian) {
                                response.sendRedirect("/librarian");
                            } else {
                                response.sendRedirect("/reader");
                            }
                        })
                        .failureUrl("/login?error")
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }
}
