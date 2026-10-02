package ru.library.libraryproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.library.libraryproject.entity.Book;

public interface BookRepository extends JpaRepository<Book, Integer> {
}