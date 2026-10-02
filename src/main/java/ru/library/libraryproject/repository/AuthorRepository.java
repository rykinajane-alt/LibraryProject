package ru.library.libraryproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.library.libraryproject.entity.Author;

public interface AuthorRepository extends JpaRepository<Author, Integer> {
}