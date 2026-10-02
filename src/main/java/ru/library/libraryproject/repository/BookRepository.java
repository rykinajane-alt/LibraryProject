package ru.library.libraryproject.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.library.libraryproject.entity.Book;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {

    @Query("""
            SELECT DISTINCT b
            FROM Book b
            LEFT JOIN b.authors a
            WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :query, '%'))
               OR LOWER(a.firstName) LIKE LOWER(CONCAT('%', :query, '%'))
               OR LOWER(a.lastName) LIKE LOWER(CONCAT('%', :query, '%'))
            """)
    List<Book> searchByTitleOrAuthor(
            @Param("query") String query
    );

    List<Book> findAll(Sort sort);

    boolean existsByIsbn(String isbn);

    boolean existsByIsbnAndIdNot(
            String isbn,
            Integer id
    );
}