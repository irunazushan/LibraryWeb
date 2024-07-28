package com.ilshan.library_web.dto;

import com.ilshan.library_web.models.Book;
import com.ilshan.library_web.models.Person;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class DTOConverters {
    private final ModelMapper modelMapper;

    public DTOConverters(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Person convertToPerson(PersonDTO personDTO) {
        return modelMapper.map(personDTO, Person.class);
    }

    public PersonDTO convertToPersonDTO(Person person) {
        return modelMapper.map(person, PersonDTO.class);
    }

    public Book convertToBook(BookDTO bookDTO) {
        return modelMapper.map(bookDTO, Book.class);
    }

    public BookDTO convertToBookDTO(Book book) {
        return modelMapper.map(book, BookDTO.class);
    }
}
