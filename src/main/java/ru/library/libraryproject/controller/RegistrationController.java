package ru.library.libraryproject.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.library.libraryproject.entity.Role;
import ru.library.libraryproject.entity.User;
import ru.library.libraryproject.repository.RoleRepository;
import ru.library.libraryproject.repository.UserRepository;

import java.time.LocalDateTime;

@Controller
public class RegistrationController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationController(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register";
    }

    @PostMapping("/register")
    public String register(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String login,
            @RequestParam String password,
            Model model) {

        if (userRepository.findByLogin(login).isPresent()) {
            model.addAttribute("error", "Пользователь с таким логином уже существует.");
            return "register";
        }

        Role readerRole = roleRepository.findByName("READER")
                .orElseThrow(() ->
                        new IllegalStateException("Роль READER не найдена"));

        User user = new User();

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setLogin(login);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setRole(readerRole);
        user.setRegisteredAt(LocalDateTime.now());
        user.setStatus("ACTIVE");

        userRepository.save(user);

        return "redirect:/login?registered";
    }
}