package ru.library.libraryproject.service;

import org.springframework.stereotype.Service;
import ru.library.libraryproject.entity.Book;
import ru.library.libraryproject.entity.Reservation;
import ru.library.libraryproject.entity.User;
import ru.library.libraryproject.repository.BookRepository;
import ru.library.libraryproject.repository.ReservationRepository;
import ru.library.libraryproject.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public ReservationService(
            ReservationRepository reservationRepository,
            UserRepository userRepository,
            BookRepository bookRepository) {

        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    // Бронирования текущего пользователя
    public List<Reservation> getReservationsForUser(String login) {
        return reservationRepository.findByUser_LoginOrderByReservationDateDesc(login);
    }

    // Проверка, есть ли у пользователя активная бронь на книгу
    public boolean hasActiveReservation(String login, Integer bookId) {
        return reservationRepository.existsByUser_LoginAndBook_IdAndStatus(
                login,
                bookId,
                "ACTIVE"
        );
    }

    // Создание бронирования
    public void createReservation(String login, Integer bookId) {

        if (hasActiveReservation(login, bookId)) {
            throw new IllegalStateException(
                    "Вы уже забронировали эту книгу."
            );
        }

        User user = userRepository.findByLogin(login)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Пользователь не найден: " + login
                        )
                );

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Книга не найдена."
                        )
                );

        Reservation reservation = new Reservation();

        reservation.setUser(user);
        reservation.setBook(book);
        reservation.setReservationDate(LocalDateTime.now());
        reservation.setStatus("ACTIVE");

        reservationRepository.save(reservation);
    }

    // Отмена бронирования
    public void cancelReservation(String login, Integer reservationId) {

        Reservation reservation = reservationRepository
                .findByIdAndUser_Login(reservationId, login)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Бронирование не найдено."
                        )
                );

        if (!"ACTIVE".equals(reservation.getStatus())) {
            throw new IllegalStateException(
                    "Это бронирование уже нельзя отменить."
            );
        }

        reservation.setStatus("CANCELLED");

        reservationRepository.save(reservation);
    }

    // Активные бронирования для библиотекаря
    public List<Reservation> getActiveReservations() {
        return reservationRepository.findByStatusOrderByReservationDateAsc(
                "ACTIVE"
        );
    }
}