package com.ilshan.library_web.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "book")
public class Book {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    @NotEmpty(message = "Название книги не может быть пустым")
    private String name;

    @Column(name = "author")
    @NotEmpty(message = "Имя автора не может быть пустым")
    private String author;

    @Column(name = "year")
    @Range(min = 1800, max = 2024, message = "Год написания книги должен быть в пределе от 1800 до 2024 года")
    private int year;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person owner;

    @Column(name = "owned_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ownedTime;

    @Transient
    private boolean isExpired;

    public Book() {
    }

    public Book(int id, String name, String author, int year) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.year = year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return id == book.id && year == book.year && Objects.equals(name, book.name) && Objects.equals(author, book.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, author, year);
    }

    @Override
    public String toString() {
        return "{" +
                "id: " + id +
                ", name: " + name +
                ", author: " + author +
                ", year: " + year +
                '}';
    }
}
