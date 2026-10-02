package ru.library.libraryproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminAboutController {

    @GetMapping("/admin/about")
    public String aboutPage() {
        return "admin-about";
    }
}