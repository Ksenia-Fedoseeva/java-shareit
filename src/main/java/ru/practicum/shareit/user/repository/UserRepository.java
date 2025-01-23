package ru.practicum.shareit.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.user.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByEmail(String email);

    @Modifying
    @Query("UPDATE User u SET " +
            "u.name = COALESCE(:name, u.name), " +
            "u.email = COALESCE(:email, u.email) " +
            "WHERE u.id = :id")
    void updateUser(@Param("id") Long id, @Param("name") String name, @Param("email") String email);
}
