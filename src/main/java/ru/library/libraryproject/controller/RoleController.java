package ru.library.libraryproject.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.library.libraryproject.service.LoanService;
import ru.library.libraryproject.service.ReservationService;
import ru.library.libraryproject.service.UserService;

@Controller
public class RoleController {

    private final LoanService loanService;
    private final ReservationService reservationService;
    private final UserService userService;

    public RoleController(
            LoanService loanService,
            ReservationService reservationService,
            UserService userService) {

        this.loanService = loanService;
        this.reservationService = reservationService;
        this.userService = userService;
    }

    // =========================
    // ЧИТАТЕЛЬ
    // =========================

    @GetMapping("/reader")
    public String readerPage() {
        return "reader";
    }

    @GetMapping("/reader/books")
    public String myBooks(
            Authentication authentication,
            Model model) {

        String login = authentication.getName();

        model.addAttribute(
                "loans",
                loanService.getLoansForUser(login)
        );

        return "my-books";
    }

    @GetMapping("/reader/reservations")
    public String myReservations(
            Authentication authentication,
            Model model) {

        String login = authentication.getName();

        model.addAttribute(
                "reservations",
                reservationService.getReservationsForUser(login)
        );

        return "my-reservations";
    }

    @PostMapping("/reader/reservations/{id}/cancel")
    public String cancelReservation(
            @PathVariable Integer id,
            Authentication authentication) {

        reservationService.cancelReservation(
                authentication.getName(),
                id
        );

        return "redirect:/reader/reservations";
    }

    @GetMapping("/reader/profile")
    public String profile(
            Authentication authentication,
            Model model) {

        String login = authentication.getName();

        model.addAttribute(
                "user",
                userService.getUserByLogin(login)
        );

        return "profile";
    }


    // =========================
    // БИБЛИОТЕКАРЬ
    // =========================

    @GetMapping("/librarian")
    public String librarianPage() {
        return "librarian";
    }

    @GetMapping("/librarian/reservations")
    public String librarianReservations(Model model) {

        model.addAttribute(
                "reservations",
                reservationService.getActiveReservations()
        );

        return "librarian-reservations";
    }

    // Выдача книги по бронированию
    @PostMapping("/librarian/reservations/{id}/issue")
    public String issueBook(
            @PathVariable Integer id,
            RedirectAttributes redirectAttributes) {

        try {

            loanService.issueBook(id);

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Книга успешно выдана читателю."
            );

        } catch (IllegalStateException e) {

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    e.getMessage()
            );
        }

        return "redirect:/librarian/reservations";
    }


    // =========================
    // АДМИНИСТРАТОР
    // =========================

    @GetMapping("/admin")
    public String adminPage() {
        return "admin";
    }


    // =========================
    // ВРЕМЕННЫЕ ТЕСТОВЫЕ СТРАНИЦЫ
    // =========================

    @GetMapping("/reader/test")
    public String readerTest() {
        return "reader-test";
    }

    @GetMapping("/librarian/test")
    public String librarianTest() {
        return "librarian-test";
    }

    @GetMapping("/admin/test")
    public String adminTest() {
        return "admin-test";
    }
}