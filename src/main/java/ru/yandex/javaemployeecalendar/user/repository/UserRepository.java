package ru.yandex.javaemployeecalendar.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.javaemployeecalendar.user.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);
    boolean existsByEmail(String email);
}
