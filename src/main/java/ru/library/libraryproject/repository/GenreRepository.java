package ru.library.libraryproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.library.libraryproject.entity.Genre;

public interface GenreRepository extends JpaRepository<Genre, Integer> {

    boolean existsByName(String name);
}