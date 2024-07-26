package com.ilshan.library_web.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "person")
public class Person {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "fullname")
    @NotEmpty(message = "ФИО не может быть пустым")
    @Pattern(regexp = "[А-Я][а-я]+ [А-Я][а-я]+ [А-Я][а-я]+", message = "ФИО должно быть написано в следующем формате: Иванов Иван Иванович")
    private String fullName;

    @Column(name = "birth_date")
    @Range(min = 1900, max = 2024, message = "Год рождения должен быть в пределе от 1900 до 2024 года")
    private int birthDate;

    @OneToMany(mappedBy = "owner")
    private List<Book> books;

    public Person() {
    }

    public Person(int id, String fullName, int birthDate) {
        this.id = id;
        this.fullName = fullName;
        this.birthDate = birthDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return id == person.id && birthDate == person.birthDate && Objects.equals(fullName, person.fullName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fullName, birthDate);
    }

    @Override
    public String toString() {
        return "{" +
                "id: " + id +
                ", fullName: " + fullName +
                ", birthDate: " + birthDate +
                '}';
    }

}
