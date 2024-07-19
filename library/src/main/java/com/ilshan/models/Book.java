package com.ilshan.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class Book {
    private int id;
    @NotEmpty(message = "Название книги не может быть пустым")
    private String name;
    @NotEmpty(message = "Имя автора не может быть пустым")
    @Pattern(regexp = "[А-Я][а-я]+ [А-Я][а-я]+", message = "Имя автора должно быть написано в следующем формате: Владимир Пелевин")
    private String author;
    @Range(min = 1800, max = 2024, message = "Год написания книги должен быть в пределе от 1800 до 2024 года")
    private int year;

    public Book() {}

    public Book(int id, String name, String author, int year) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.year = year;
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
