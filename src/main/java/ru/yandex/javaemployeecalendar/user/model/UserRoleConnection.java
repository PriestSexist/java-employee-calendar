package ru.yandex.javaemployeecalendar.user.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_role_connection")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRoleConnection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "user_id")
    private int user_id;
    @Column(name = "role_id")
    private int role_id;
}
