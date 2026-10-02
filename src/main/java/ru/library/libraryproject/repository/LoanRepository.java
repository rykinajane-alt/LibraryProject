package ru.library.libraryproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.library.libraryproject.entity.Loan;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Integer> {

    List<Loan> findByUser_LoginOrderByIssueDateDesc(String login);

    List<Loan> findByStatusOrderByDueDateAsc(String status);

    long countByStatus(String status);
}