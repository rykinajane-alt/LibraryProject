package ru.library.libraryproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.library.libraryproject.service.StatisticsService;

@Controller
public class AdminStatisticsController {

    private final StatisticsService statisticsService;

    public AdminStatisticsController(
            StatisticsService statisticsService) {

        this.statisticsService = statisticsService;
    }

    @GetMapping("/admin/statistics")
    public String statisticsPage(Model model) {

        model.addAttribute(
                "totalBooks",
                statisticsService.getTotalBooks()
        );

        model.addAttribute(
                "totalCopies",
                statisticsService.getTotalCopies()
        );

        model.addAttribute(
                "availableCopies",
                statisticsService.getAvailableCopies()
        );

        model.addAttribute(
                "issuedCopies",
                statisticsService.getIssuedCopies()
        );

        model.addAttribute(
                "totalUsers",
                statisticsService.getTotalUsers()
        );

        model.addAttribute(
                "readers",
                statisticsService.getReaders()
        );

        model.addAttribute(
                "activeReservations",
                statisticsService.getActiveReservations()
        );

        model.addAttribute(
                "activeLoans",
                statisticsService.getActiveLoans()
        );

        return "admin-statistics";
    }
}