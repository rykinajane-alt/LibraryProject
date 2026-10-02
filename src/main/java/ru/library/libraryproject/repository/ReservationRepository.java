package ru.library.libraryproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.library.libraryproject.entity.Reservation;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    List<Reservation> findByUser_LoginOrderByReservationDateDesc(String login);

    boolean existsByUser_LoginAndBook_IdAndStatus(
            String login,
            Integer bookId,
            String status
    );

    Optional<Reservation> findByIdAndUser_Login(
            Integer id,
            String login
    );

    List<Reservation> findByStatusOrderByReservationDateAsc(
            String status
    );

    long countByStatus(String status);
}