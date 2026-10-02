package ru.library.libraryproject.service;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.library.libraryproject.entity.Book;
import ru.library.libraryproject.repository.BookCopyRepository;
import ru.library.libraryproject.repository.BookRepository;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final BookCopyRepository bookCopyRepository;

    public BookService(
            BookRepository bookRepository,
            BookCopyRepository bookCopyRepository) {

        this.bookRepository = bookRepository;
        this.bookCopyRepository = bookCopyRepository;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public List<Book> searchBooks(String query) {
        return bookRepository.searchByTitleOrAuthor(query);
    }

    public List<Book> getBooksSorted(String sort) {

        if ("titleAsc".equals(sort)) {
            return bookRepository.findAll(
                    Sort.by("title").ascending()
            );
        }

        if ("titleDesc".equals(sort)) {
            return bookRepository.findAll(
                    Sort.by("title").descending()
            );
        }

        if ("yearAsc".equals(sort)) {
            return bookRepository.findAll(
                    Sort.by("publicationYear").ascending()
            );
        }

        if ("yearDesc".equals(sort)) {
            return bookRepository.findAll(
                    Sort.by("publicationYear").descending()
            );
        }

        return bookRepository.findAll();
    }

    // Получение книги по ID
    public Book getBookById(Integer bookId) {

        return bookRepository.findById(bookId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Книга не найдена."
                        )
                );
    }

    // Проверка наличия свободного экземпляра книги
    public boolean isBookAvailable(Integer bookId) {

        return bookCopyRepository.countByBookIdAndStatus(
                bookId,
                "AVAILABLE"
        ) > 0;
    }

    // Общее количество экземпляров книги
    public long getTotalCopies(Integer bookId) {

        return bookCopyRepository.countByBookIdAndStatus(
                bookId,
                "AVAILABLE"
        ) + bookCopyRepository.countByBookIdAndStatus(
                bookId,
                "ISSUED"
        );
    }

    // Количество доступных экземпляров книги
    public long getAvailableCopies(Integer bookId) {

        return bookCopyRepository.countByBookIdAndStatus(
                bookId,
                "AVAILABLE"
        );
    }

    // Количество выданных экземпляров книги
    public long getIssuedCopies(Integer bookId) {

        return bookCopyRepository.countByBookIdAndStatus(
                bookId,
                "ISSUED"
        );
    }
}