package com.ilshan.library_web.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import java.util.List;
import java.util.Objects;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "email")
    @Email
    @NotEmpty(message = "Логин не может быть пустым")
    @Size(max = 100, message = "Логин должен иметь менее 100 символов")
    private String email;

    @NotEmpty(message = "Пароль не может быть пустым")
    @Column(name = "password")
    private String password;

    @Column(name = "authority")
    @NotEmpty(message = "Роль должна быть определена")
    @Enumerated(value = EnumType.STRING)
    private Role role;

    public User(){};

    public User(int id, String email, String password, Role role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
