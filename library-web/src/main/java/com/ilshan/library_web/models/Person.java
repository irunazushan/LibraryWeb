package com.ilshan.library_web.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(schema = "library_web", name = "t_person")
public class Person {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "c_fullname")
    @NotEmpty(message = "ФИО не может быть пустым")
    @Pattern(regexp = "[А-Я][а-я]+ [А-Я][а-я]+ [А-Я][а-я]+", message = "ФИО должно быть написано в следующем формате: Иванов Иван Иванович")
    private String fullName;

    @Column(name = "c_birth_date")
//    @NotEmpty(message = "Год рождения не может быть пустым")
    @Range(min = 1900, max = 2024, message = "Год рождения должен быть в пределе от 1900 до 2024 года")
    private int birthDate;

    @Column(name = "c_created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date cretedAt;

    @Column(name = "c_updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @Column(name = "c_created_by")
    @Size(max = 100)
    private String createdBy;

    @JsonManagedReference
    @OneToMany(mappedBy = "owner", cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
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
