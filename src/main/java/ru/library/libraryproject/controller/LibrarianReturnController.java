package ru.library.libraryproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.library.libraryproject.service.LoanService;

@Controller
public class LibrarianReturnController {

    private final LoanService loanService;

    public LibrarianReturnController(
            LoanService loanService) {

        this.loanService = loanService;
    }


    // Страница возврата книг
    @GetMapping("/librarian/returns")
    public String returnsPage(Model model) {

        model.addAttribute(
                "loans",
                loanService.getActiveLoans()
        );

        return "librarian-returns";
    }


    // Оформление возврата
    @PostMapping("/librarian/returns/{id}")
    public String returnBook(
            @PathVariable Integer id,
            RedirectAttributes redirectAttributes) {

        try {

            loanService.returnBook(id);

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Книга успешно возвращена."
            );

        } catch (RuntimeException e) {

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    e.getMessage()
            );
        }

        return "redirect:/librarian/returns";
    }
}