package ru.library.libraryproject.service;

import org.springframework.stereotype.Service;
import ru.library.libraryproject.entity.Author;
import ru.library.libraryproject.entity.Book;
import ru.library.libraryproject.entity.BookCopy;
import ru.library.libraryproject.entity.Genre;
import ru.library.libraryproject.repository.AuthorRepository;
import ru.library.libraryproject.repository.BookCopyRepository;
import ru.library.libraryproject.repository.BookRepository;
import ru.library.libraryproject.repository.GenreRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookManagementService {

    private final BookRepository bookRepository;
    private final BookCopyRepository bookCopyRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;

    public BookManagementService(
            BookRepository bookRepository,
            BookCopyRepository bookCopyRepository,
            AuthorRepository authorRepository,
            GenreRepository genreRepository) {

        this.bookRepository = bookRepository;
        this.bookCopyRepository = bookCopyRepository;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
    }


    // =========================================
    // ПОЛУЧЕНИЕ АВТОРОВ
    // =========================================

    public List<Author> getAllAuthors() {

        return authorRepository.findAll();
    }


    // =========================================
    // ПОЛУЧЕНИЕ ЖАНРОВ
    // =========================================

    public List<Genre> getAllGenres() {

        return genreRepository.findAll();
    }


    // =========================================
    // ДОБАВЛЕНИЕ АВТОРА
    // =========================================

    public void addAuthor(
            String firstName,
            String lastName,
            String middleName) {

        Author author = new Author();

        author.setFirstName(firstName);
        author.setLastName(lastName);
        author.setMiddleName(middleName);

        authorRepository.save(author);
    }


    // =========================================
    // ДОБАВЛЕНИЕ ЖАНРА
    // =========================================

    public void addGenre(String name) {

        if (name == null || name.isBlank()) {

            throw new IllegalStateException(
                    "Название жанра не может быть пустым."
            );
        }

        String genreName = name.trim();

        if (genreRepository.existsByName(genreName)) {

            throw new IllegalStateException(
                    "Жанр с таким названием уже существует."
            );
        }

        Genre genre = new Genre();

        genre.setName(genreName);

        genreRepository.save(genre);
    }


    // =========================================
    // ДОБАВЛЕНИЕ КНИГИ
    // =========================================

    public void addBook(
            String title,
            String isbn,
            Integer publicationYear,
            String description,
            Integer authorId,
            Integer genreId,
            Integer copiesCount) {

        Author author = authorRepository.findById(authorId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Автор не найден."
                        )
                );

        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Жанр не найден."
                        )
                );


        if (isbn != null && isbn.isBlank()) {

            isbn = null;
        }


        if (isbn != null
                && bookRepository.existsByIsbn(isbn)) {

            throw new IllegalStateException(
                    "Книга с таким ISBN уже существует."
            );
        }


        Book book = new Book();

        book.setTitle(title);
        book.setIsbn(isbn);
        book.setPublicationYear(publicationYear);
        book.setDescription(description);
        book.setGenre(genre);


        List<Author> authors = new ArrayList<>();

        authors.add(author);

        book.setAuthors(authors);


        book = bookRepository.save(book);


        for (int i = 1; i <= copiesCount; i++) {

            BookCopy copy = new BookCopy();

            copy.setBook(book);

            copy.setInventoryNumber(
                    String.format(
                            "BK-%03d-%d",
                            book.getId(),
                            i
                    )
            );

            copy.setStatus("AVAILABLE");

            bookCopyRepository.save(copy);
        }
    }


    // =========================================
    // РЕДАКТИРОВАНИЕ КНИГИ
    // =========================================

    public void updateBook(
            Integer bookId,
            String title,
            String isbn,
            Integer publicationYear,
            String description,
            Integer authorId,
            Integer genreId) {

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Книга не найдена."
                        )
                );


        Author author = authorRepository.findById(authorId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Автор не найден."
                        )
                );


        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Жанр не найден."
                        )
                );


        if (isbn != null && isbn.isBlank()) {

            isbn = null;
        }


        if (isbn != null
                && bookRepository.existsByIsbnAndIdNot(
                isbn,
                bookId)) {

            throw new IllegalStateException(
                    "Книга с таким ISBN уже существует."
            );
        }


        book.setTitle(title);
        book.setIsbn(isbn);
        book.setPublicationYear(publicationYear);
        book.setDescription(description);
        book.setGenre(genre);


        List<Author> authors = new ArrayList<>();

        authors.add(author);

        book.setAuthors(authors);


        bookRepository.saveAndFlush(book);
    }
}