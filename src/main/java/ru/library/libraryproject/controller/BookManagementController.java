package ru.library.libraryproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.library.libraryproject.entity.Book;
import ru.library.libraryproject.service.BookManagementService;
import ru.library.libraryproject.service.BookService;

@Controller
public class BookManagementController {

    private final BookManagementService bookManagementService;
    private final BookService bookService;

    public BookManagementController(
            BookManagementService bookManagementService,
            BookService bookService) {

        this.bookManagementService = bookManagementService;
        this.bookService = bookService;
    }


    // =========================================
    // ДОБАВЛЕНИЕ КНИГИ
    // =========================================

    @GetMapping("/librarian/books/add")
    public String addBookPage(Model model) {

        model.addAttribute(
                "authors",
                bookManagementService.getAllAuthors()
        );

        model.addAttribute(
                "genres",
                bookManagementService.getAllGenres()
        );

        return "add-book";
    }


    @PostMapping("/librarian/books/add")
    public String addBook(
            @RequestParam String title,
            @RequestParam(required = false) String isbn,
            @RequestParam(required = false) Integer publicationYear,
            @RequestParam(required = false) String description,
            @RequestParam Integer authorId,
            @RequestParam Integer genreId,
            @RequestParam Integer copiesCount,
            RedirectAttributes redirectAttributes) {

        try {

            bookManagementService.addBook(
                    title,
                    isbn,
                    publicationYear,
                    description,
                    authorId,
                    genreId,
                    copiesCount
            );

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Книга успешно добавлена."
            );

            return "redirect:/books";

        } catch (RuntimeException e) {

            e.printStackTrace();

            String message = getErrorMessage(e);

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Не удалось добавить книгу: " + message
            );

            return "redirect:/librarian/books/add";
        }
    }


    // =========================================
    // ДОБАВЛЕНИЕ АВТОРА
    // =========================================

    @GetMapping("/librarian/authors/add")
    public String addAuthorPage() {

        return "add-author";
    }


    @PostMapping("/librarian/authors/add")
    public String addAuthor(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam(required = false) String middleName,
            RedirectAttributes redirectAttributes) {

        try {

            bookManagementService.addAuthor(
                    firstName,
                    lastName,
                    middleName
            );

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Автор успешно добавлен."
            );

            return "redirect:/librarian/books/add";

        } catch (RuntimeException e) {

            e.printStackTrace();

            String message = getErrorMessage(e);

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Не удалось добавить автора: " + message
            );

            return "redirect:/librarian/authors/add";
        }
    }


    // =========================================
    // ДОБАВЛЕНИЕ ЖАНРА
    // =========================================

    @GetMapping("/librarian/genres/add")
    public String addGenrePage() {

        return "add-genre";
    }


    @PostMapping("/librarian/genres/add")
    public String addGenre(
            @RequestParam String name,
            RedirectAttributes redirectAttributes) {

        try {

            bookManagementService.addGenre(name);

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Жанр успешно добавлен."
            );

            return "redirect:/librarian/books/add";

        } catch (RuntimeException e) {

            e.printStackTrace();

            String message = getErrorMessage(e);

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Не удалось добавить жанр: " + message
            );

            return "redirect:/librarian/genres/add";
        }
    }


    // =========================================
    // РЕДАКТИРОВАНИЕ КНИГИ
    // =========================================

    @GetMapping("/librarian/books/{id}/edit")
    public String editBookPage(
            @PathVariable Integer id,
            Model model) {

        Book book = bookService.getBookById(id);

        model.addAttribute(
                "book",
                book
        );

        model.addAttribute(
                "authors",
                bookManagementService.getAllAuthors()
        );

        model.addAttribute(
                "genres",
                bookManagementService.getAllGenres()
        );

        return "edit-book";
    }


    @PostMapping("/librarian/books/{id}/edit")
    public String editBook(
            @PathVariable Integer id,
            @RequestParam String title,
            @RequestParam(required = false) String isbn,
            @RequestParam(required = false) Integer publicationYear,
            @RequestParam(required = false) String description,
            @RequestParam Integer authorId,
            @RequestParam Integer genreId,
            RedirectAttributes redirectAttributes) {

        try {

            bookManagementService.updateBook(
                    id,
                    title,
                    isbn,
                    publicationYear,
                    description,
                    authorId,
                    genreId
            );

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Изменения книги сохранены."
            );

            return "redirect:/books/" + id;

        } catch (RuntimeException e) {

            e.printStackTrace();

            String message = getErrorMessage(e);

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Не удалось сохранить изменения: " + message
            );

            return "redirect:/librarian/books/"
                    + id
                    + "/edit";
        }
    }


    // =========================================
    // ПОЛУЧЕНИЕ ТЕКСТА ОШИБКИ
    // =========================================

    private String getErrorMessage(
            Throwable exception) {

        Throwable current = exception;

        while (current != null) {

            if (current.getMessage() != null
                    && !current.getMessage().isBlank()) {

                return current.getMessage();
            }

            current = current.getCause();
        }

        return "Неизвестная ошибка. Подробности смотрите в консоли IntelliJ IDEA.";
    }
}