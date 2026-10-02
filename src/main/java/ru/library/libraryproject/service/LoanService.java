package ru.library.libraryproject.service;

import org.springframework.stereotype.Service;
import ru.library.libraryproject.entity.Book;
import ru.library.libraryproject.entity.BookCopy;
import ru.library.libraryproject.entity.Loan;
import ru.library.libraryproject.entity.Reservation;
import ru.library.libraryproject.entity.User;
import ru.library.libraryproject.repository.BookCopyRepository;
import ru.library.libraryproject.repository.BookRepository;
import ru.library.libraryproject.repository.LoanRepository;
import ru.library.libraryproject.repository.ReservationRepository;
import ru.library.libraryproject.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final ReservationRepository reservationRepository;
    private final BookCopyRepository bookCopyRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public LoanService(
            LoanRepository loanRepository,
            ReservationRepository reservationRepository,
            BookCopyRepository bookCopyRepository,
            UserRepository userRepository,
            BookRepository bookRepository) {

        this.loanRepository = loanRepository;
        this.reservationRepository = reservationRepository;
        this.bookCopyRepository = bookCopyRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    // Получение книг конкретного читателя
    public List<Loan> getLoansForUser(String login) {

        return loanRepository.findByUser_LoginOrderByIssueDateDesc(
                login
        );
    }


    // Получение всех активных выдач
    public List<Loan> getActiveLoans() {

        return loanRepository.findByStatusOrderByDueDateAsc(
                "ACTIVE"
        );
    }


    // Выдача книги по бронированию
    public void issueBook(Integer reservationId) {

        Reservation reservation = reservationRepository
                .findById(reservationId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Бронирование не найдено."
                        )
                );

        if (!"ACTIVE".equals(reservation.getStatus())) {

            throw new IllegalStateException(
                    "Это бронирование уже нельзя обработать."
            );
        }

        Integer bookId = reservation.getBook().getId();

        BookCopy copy = bookCopyRepository
                .findFirstByBookIdAndStatus(
                        bookId,
                        "AVAILABLE"
                )
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Нет свободного экземпляра этой книги."
                        )
                );

        LocalDate issueDate = LocalDate.now();

        LocalDate dueDate = issueDate.plusDays(10);

        Loan loan = new Loan();

        loan.setUser(reservation.getUser());
        loan.setCopy(copy);
        loan.setIssueDate(issueDate);
        loan.setDueDate(dueDate);
        loan.setReturnDate(null);
        loan.setStatus("ACTIVE");

        loanRepository.save(loan);

        copy.setStatus("ISSUED");

        bookCopyRepository.save(copy);

        reservation.setStatus("COMPLETED");

        reservationRepository.save(reservation);
    }


    // Обычная выдача книги без предварительного бронирования
    public void issueBookDirectly(
            Integer userId,
            Integer bookId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Читатель не найден."
                        )
                );

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Книга не найдена."
                        )
                );

        BookCopy copy = bookCopyRepository
                .findFirstByBookIdAndStatus(
                        bookId,
                        "AVAILABLE"
                )
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Нет свободных экземпляров этой книги."
                        )
                );

        LocalDate issueDate = LocalDate.now();

        LocalDate dueDate = issueDate.plusDays(10);

        Loan loan = new Loan();

        loan.setUser(user);
        loan.setCopy(copy);
        loan.setIssueDate(issueDate);
        loan.setDueDate(dueDate);
        loan.setReturnDate(null);
        loan.setStatus("ACTIVE");

        loanRepository.save(loan);

        copy.setStatus("ISSUED");

        bookCopyRepository.save(copy);
    }


    // Возврат книги
    public void returnBook(Integer loanId) {

        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Выдача не найдена."
                        )
                );

        if (!"ACTIVE".equals(loan.getStatus())) {

            throw new IllegalStateException(
                    "Эта книга уже возвращена."
            );
        }

        loan.setReturnDate(
                LocalDate.now()
        );

        loan.setStatus("RETURNED");

        loanRepository.save(loan);


        BookCopy copy = loan.getCopy();

        copy.setStatus("AVAILABLE");

        bookCopyRepository.save(copy);
    }
}