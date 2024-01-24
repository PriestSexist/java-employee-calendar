package ru.yandex.javaemployeecalendar.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.javaemployeecalendar.user.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
}
