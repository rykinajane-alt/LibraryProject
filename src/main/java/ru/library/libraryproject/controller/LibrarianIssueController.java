package ru.library.libraryproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.library.libraryproject.entity.Book;
import ru.library.libraryproject.entity.User;
import ru.library.libraryproject.repository.BookRepository;
import ru.library.libraryproject.repository.UserRepository;
import ru.library.libraryproject.service.LoanService;

import java.util.List;

@Controller
public class LibrarianIssueController {

    private final LoanService loanService;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public LibrarianIssueController(
            LoanService loanService,
            UserRepository userRepository,
            BookRepository bookRepository) {

        this.loanService = loanService;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    // Страница выдачи книги
    @GetMapping("/librarian/issue")
    public String issuePage(Model model) {

        List<User> readers =
                userRepository.findByRole_Name("READER");

        List<Book> books =
                bookRepository.findAll();

        model.addAttribute(
                "readers",
                readers
        );

        model.addAttribute(
                "books",
                books
        );

        return "librarian-issue";
    }

    // Оформление выдачи
    @PostMapping("/librarian/issue")
    public String issueBook(
            @RequestParam Integer userId,
            @RequestParam Integer bookId,
            RedirectAttributes redirectAttributes) {

        try {

            loanService.issueBookDirectly(
                    userId,
                    bookId
            );

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Книга успешно выдана."
            );

        } catch (RuntimeException e) {

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    e.getMessage()
            );
        }

        return "redirect:/librarian/issue";
    }
}