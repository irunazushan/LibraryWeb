package com.ilshan.library_web.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
public class PersonDTO {
    @NotEmpty(message = "ФИО не может быть пустым")
    @Pattern(regexp = "[А-Я][а-я]+ [А-Я][а-я]+ [А-Я][а-я]+", message = "ФИО должно быть написано в следующем формате: Иванов Иван Иванович")
    private String fullName;

    @Column(name = "birth_date")
    @Range(min = 1900, max = 2024, message = "Год рождения должен быть в пределе от 1900 до 2024 года")
    private int birthDate;
}
