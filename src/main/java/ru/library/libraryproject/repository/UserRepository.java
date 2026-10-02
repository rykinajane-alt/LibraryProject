package ru.library.libraryproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.library.libraryproject.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByLogin(String login);
}