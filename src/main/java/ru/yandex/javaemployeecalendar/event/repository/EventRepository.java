package ru.yandex.javaemployeecalendar.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.javaemployeecalendar.event.model.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
}
