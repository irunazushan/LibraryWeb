package com.ilshan.library_web.controllers;

import com.ilshan.library_web.dto.PersonDTO;
import com.ilshan.library_web.models.Book;
import com.ilshan.library_web.models.Person;
import com.ilshan.library_web.services.BooksService;
import com.ilshan.library_web.services.PeopleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class APIContoller {
    private final PeopleService peopleService;
    private final BooksService booksService;
    private final ModelMapper modelMapper;

    @Autowired
    public APIContoller(PeopleService peopleService, BooksService booksService, ModelMapper modelMapper) {
        this.peopleService = peopleService;
        this.booksService = booksService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/people")
    public List<PersonDTO> getPeople() {
        return peopleService.findAll().stream()
                .map(this::convertToPersonDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/people/{id}")
    public PersonDTO getPerson(@PathVariable("id") int id) {
        return convertToPersonDTO(peopleService.findOne(id));
    }

    @GetMapping("/books")
    public List<Book> getBooks() {
        return booksService.findAll();
    }

    @GetMapping("/books/{id}")
    public Book getBook(@PathVariable("id") int id) {
        return booksService.findOne(id);
    }

    private Person convertToPerson(PersonDTO personDTO) {
        return modelMapper.map(personDTO, Person.class);
    }

    private PersonDTO convertToPersonDTO(Person person) {
        return modelMapper.map(person, PersonDTO.class);
    }
}
