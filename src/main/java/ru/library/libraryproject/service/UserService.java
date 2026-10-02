package ru.library.libraryproject.service;

import org.springframework.stereotype.Service;
import ru.library.libraryproject.entity.Role;
import ru.library.libraryproject.entity.User;
import ru.library.libraryproject.repository.RoleRepository;
import ru.library.libraryproject.repository.UserRepository;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserService(
            UserRepository userRepository,
            RoleRepository roleRepository) {

        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public User getUserByLogin(String login) {

        return userRepository.findByLogin(login)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Пользователь не найден: " + login
                        )
                );
    }

    public List<User> getAllUsers() {

        return userRepository.findAll();
    }

    public List<Role> getAllRoles() {

        return roleRepository.findAll();
    }

    public void updateRole(
            Integer userId,
            Integer roleId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Пользователь не найден."
                        )
                );

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Роль не найдена."
                        )
                );

        user.setRole(role);

        userRepository.save(user);
    }

    public void updateStatus(
            Integer userId,
            String status) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Пользователь не найден."
                        )
                );

        user.setStatus(status);

        userRepository.save(user);
    }
}