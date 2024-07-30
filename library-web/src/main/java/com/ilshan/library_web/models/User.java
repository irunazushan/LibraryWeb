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
@Table(schema = "library_web", name = "t_user")
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "c_email")
    @Email
    @NotEmpty(message = "Логин не может быть пустым")
    @Size(max = 100, message = "Логин должен иметь менее 100 символов")
    private String email;

    @Column(name = "c_password")
    @NotEmpty(message = "Пароль не может быть пустым")
    private String password;

    @Column(name = "c_authority")
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
