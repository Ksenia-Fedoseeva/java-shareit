package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBookerIdOrderByStartDesc(Long userId);

    List<Booking> findByItemOwnerIdOrderByStartDesc(Long userId);

    Boolean existsByBookerAndItemAndEndBeforeAndStatus(User booker, Item item, LocalDateTime end, Status status);

    @Query("SELECT b FROM Booking b WHERE b.item.id = :itemId AND b.start < :now ORDER BY b.start DESC LIMIT 1")
    Optional<Booking> findLastBooking(@Param("itemId") Long itemId, @Param("now") LocalDateTime now);

    @Query("SELECT b FROM Booking b WHERE b.item.id = :itemId AND b.start > :now ORDER BY b.start ASC LIMIT 1")
    Optional<Booking> findNextBooking(@Param("itemId") Long itemId, @Param("now") LocalDateTime now);

    Boolean existsByItemIdAndStatusAndStartBeforeAndEndAfter(
            Long itemId, Status status, LocalDateTime end, LocalDateTime start);
}
