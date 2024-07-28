package com.ilshan.library_web.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
public class BookDTO {
    @NotEmpty(message = "Название книги не может быть пустым")
    private String name;

    @NotEmpty(message = "Имя автора не может быть пустым")
    private String author;

    @Range(min = 1800, max = 2024, message = "Год написания книги должен быть в пределе от 1800 до 2024 года")
    private int year;
}
