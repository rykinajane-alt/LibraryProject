package ru.library.libraryproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.library.libraryproject.entity.BookCopy;

import java.util.Optional;

public interface BookCopyRepository extends JpaRepository<BookCopy, Integer> {

    long countByBookIdAndStatus(Integer bookId, String status);

    Optional<BookCopy> findFirstByBookIdAndStatus(
            Integer bookId,
            String status
    );

    long countByStatus(String status);
}