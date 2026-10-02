package ru.library.libraryproject.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.library.libraryproject.entity.Book;
import ru.library.libraryproject.service.BookService;
import ru.library.libraryproject.service.ReservationService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class BookController {

    private final BookService bookService;
    private final ReservationService reservationService;

    public BookController(
            BookService bookService,
            ReservationService reservationService) {

        this.bookService = bookService;
        this.reservationService = reservationService;
    }

    @GetMapping("/books")
    public String showBooks(
            @RequestParam(required = false, defaultValue = "") String query,
            @RequestParam(required = false, defaultValue = "") String sort,
            Authentication authentication,
            Model model) {

        List<Book> books;

        if (!query.isBlank()) {
            books = bookService.searchBooks(query);
        } else {
            books = bookService.getBooksSorted(sort);
        }

        Map<Integer, Boolean> availability = new HashMap<>();
        Map<Integer, Boolean> reserved = new HashMap<>();

        Map<Integer, Long> totalCopies = new HashMap<>();
        Map<Integer, Long> availableCopies = new HashMap<>();
        Map<Integer, Long> issuedCopies = new HashMap<>();

        String login = authentication.getName();

        for (Book book : books) {

            Integer bookId = book.getId();

            availability.put(
                    bookId,
                    bookService.isBookAvailable(bookId)
            );

            reserved.put(
                    bookId,
                    reservationService.hasActiveReservation(
                            login,
                            bookId
                    )
            );

            totalCopies.put(
                    bookId,
                    bookService.getTotalCopies(bookId)
            );

            availableCopies.put(
                    bookId,
                    bookService.getAvailableCopies(bookId)
            );

            issuedCopies.put(
                    bookId,
                    bookService.getIssuedCopies(bookId)
            );
        }

        model.addAttribute("books", books);

        model.addAttribute("availability", availability);
        model.addAttribute("reserved", reserved);

        model.addAttribute("totalCopies", totalCopies);
        model.addAttribute("availableCopies", availableCopies);
        model.addAttribute("issuedCopies", issuedCopies);

        return "books";
    }


    // =========================
    // КАРТОЧКА КНИГИ
    // =========================

    @GetMapping("/books/{id}")
    public String bookCard(
            @PathVariable Integer id,
            Authentication authentication,
            Model model) {

        Book book = bookService.getBookById(id);

        model.addAttribute(
                "book",
                book
        );

        model.addAttribute(
                "totalCopies",
                bookService.getTotalCopies(id)
        );

        model.addAttribute(
                "availableCopies",
                bookService.getAvailableCopies(id)
        );

        model.addAttribute(
                "issuedCopies",
                bookService.getIssuedCopies(id)
        );

        model.addAttribute(
                "available",
                bookService.isBookAvailable(id)
        );

        model.addAttribute(
                "reserved",
                reservationService.hasActiveReservation(
                        authentication.getName(),
                        id
                )
        );

        return "book-card";
    }


    @PostMapping("/reader/books/{id}/reserve")
    public String reserveBook(
            @PathVariable Integer id,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        try {

            reservationService.createReservation(
                    authentication.getName(),
                    id
            );

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Книга успешно забронирована."
            );

        } catch (IllegalStateException e) {

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    e.getMessage()
            );
        }

        return "redirect:/books";
    }
}