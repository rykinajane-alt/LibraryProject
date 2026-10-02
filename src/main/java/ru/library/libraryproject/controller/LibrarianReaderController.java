package ru.library.libraryproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.library.libraryproject.entity.User;
import ru.library.libraryproject.service.LoanService;
import ru.library.libraryproject.repository.UserRepository;

import java.util.List;

@Controller
public class LibrarianReaderController {

    private final UserRepository userRepository;
    private final LoanService loanService;

    public LibrarianReaderController(
            UserRepository userRepository,
            LoanService loanService) {

        this.userRepository = userRepository;
        this.loanService = loanService;
    }

    // Список читателей
    @GetMapping("/librarian/readers")
    public String readersPage(Model model) {

        List<User> readers =
                userRepository.findByRole_Name("READER");

        model.addAttribute(
                "readers",
                readers
        );

        return "readers";
    }

    // Карточка конкретного читателя
    @GetMapping("/librarian/readers/{id}")
    public String readerCard(
            @PathVariable Integer id,
            Model model) {

        User reader = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Читатель не найден."
                        )
                );

        model.addAttribute(
                "reader",
                reader
        );

        model.addAttribute(
                "loans",
                loanService.getLoansForUser(
                        reader.getLogin()
                )
        );

        return "reader-card";
    }
}