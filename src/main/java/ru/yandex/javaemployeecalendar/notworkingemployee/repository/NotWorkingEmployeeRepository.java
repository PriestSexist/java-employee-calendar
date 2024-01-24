package ru.yandex.javaemployeecalendar.notworkingemployee.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.javaemployeecalendar.notworkingemployee.model.NotWorkingEmployee;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface NotWorkingEmployeeRepository extends JpaRepository<NotWorkingEmployee, Integer> {
    Optional<NotWorkingEmployee> getByEmployeeIdAndStartTimeAfterAndEndTimeBefore(int employeeId, LocalDateTime startTime, LocalDateTime endTime);

    List<NotWorkingEmployee> getAllByEmployeeIdAndStartTimeAfterAndEndTimeBefore(int employeeId, LocalDateTime startTime, LocalDateTime endTime);

}
