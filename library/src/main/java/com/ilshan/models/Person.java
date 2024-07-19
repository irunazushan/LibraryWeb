package com.ilshan.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class Person {
    private int id;

    @NotEmpty(message = "ФИО не может быть пустым")
    @Pattern(regexp = "[А-Я][а-я]+ [А-Я][а-я]+ [А-Я][а-я]+", message = "ФИО должно быть написано в следующем формате: Иванов Иван Иванович")
    private String fullName;
    @Range(min = 1900, max = 2024, message = "Год рождения должен быть в пределе от 1900 до 2024 года")
    private int birthDate;

    public Person() {}

    public Person(int id, String fullName, int birthDate) {
        this.id = id;
        this.fullName = fullName;
        this.birthDate = birthDate;
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
