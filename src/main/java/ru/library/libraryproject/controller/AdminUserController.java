package ru.library.libraryproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.library.libraryproject.service.UserService;

@Controller
public class AdminUserController {

    private final UserService userService;

    public AdminUserController(
            UserService userService) {

        this.userService = userService;
    }

    @GetMapping("/admin/users")
    public String usersPage(Model model) {

        model.addAttribute(
                "users",
                userService.getAllUsers()
        );

        model.addAttribute(
                "roles",
                userService.getAllRoles()
        );

        return "admin-users";
    }

    @PostMapping("/admin/users/{id}/update")
    public String updateUser(
            @PathVariable Integer id,
            @RequestParam Integer roleId,
            @RequestParam String status,
            RedirectAttributes redirectAttributes) {

        try {

            userService.updateRole(id, roleId);
            userService.updateStatus(id, status);

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Данные пользователя успешно обновлены."
            );

        } catch (RuntimeException e) {

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Не удалось обновить пользователя: "
                            + e.getMessage()
            );
        }

        return "redirect:/admin/users";
    }
}