package ru.library.libraryproject.service;

import org.springframework.stereotype.Service;
import ru.library.libraryproject.repository.BookCopyRepository;
import ru.library.libraryproject.repository.BookRepository;
import ru.library.libraryproject.repository.LoanRepository;
import ru.library.libraryproject.repository.ReservationRepository;
import ru.library.libraryproject.repository.UserRepository;

@Service
public class StatisticsService {

    private final BookRepository bookRepository;
    private final BookCopyRepository bookCopyRepository;
    private final UserRepository userRepository;
    private final LoanRepository loanRepository;
    private final ReservationRepository reservationRepository;

    public StatisticsService(
            BookRepository bookRepository,
            BookCopyRepository bookCopyRepository,
            UserRepository userRepository,
            LoanRepository loanRepository,
            ReservationRepository reservationRepository) {

        this.bookRepository = bookRepository;
        this.bookCopyRepository = bookCopyRepository;
        this.userRepository = userRepository;
        this.loanRepository = loanRepository;
        this.reservationRepository = reservationRepository;
    }

    public long getTotalBooks() {
        return bookRepository.count();
    }

    public long getTotalCopies() {
        return bookCopyRepository.count();
    }

    public long getAvailableCopies() {
        return bookCopyRepository.countByStatus("AVAILABLE");
    }

    public long getIssuedCopies() {
        return bookCopyRepository.countByStatus("ISSUED");
    }

    public long getTotalUsers() {
        return userRepository.count();
    }

    public long getReaders() {
        return userRepository.countByRole_Name("READER");
    }

    public long getActiveReservations() {
        return reservationRepository.countByStatus("ACTIVE");
    }

    public long getActiveLoans() {
        return loanRepository.countByStatus("ACTIVE");
    }
}