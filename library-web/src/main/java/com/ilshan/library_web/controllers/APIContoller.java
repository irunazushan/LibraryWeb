package com.ilshan.library_web.controllers;

import com.ilshan.library_web.dto.BookDTO;
import com.ilshan.library_web.dto.DTOConverters;
import com.ilshan.library_web.dto.PersonDTO;
import com.ilshan.library_web.models.Book;
import com.ilshan.library_web.models.Person;
import com.ilshan.library_web.services.BooksService;
import com.ilshan.library_web.services.PeopleService;
import com.ilshan.library_web.util.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class APIContoller {
    private final PeopleService peopleService;
    private final BooksService booksService;
    private final PersonValidator personValidator;
    private final DTOConverters converter;

    @Autowired
    public APIContoller(PeopleService peopleService,
                        BooksService booksService,
                        PersonValidator personValidator,
                        DTOConverters converter) {
        this.peopleService = peopleService;
        this.booksService = booksService;
        this.converter = converter;
        this.personValidator = personValidator;
    }

    @GetMapping("/people")
    public List<PersonDTO> getPeople() {
        return peopleService.findAll().stream()
                .map(converter::convertToPersonDTO)
                .collect(Collectors.toList());
    }

    @PostMapping("/people")
    public ResponseEntity<HttpStatus> savePerson(
            @RequestBody @Valid PersonDTO personDTO,
            BindingResult bindingResult) {
        Person person = converter.convertToPerson(personDTO);
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error: errors) {
                errorMessage.append(error.getField()).append(" - ")
                        .append(error.getDefaultMessage()).append("; ");
            }
            throw new PersonNotCreatedException(errorMessage.toString());
        }
        person.setCreatedBy("anonymous");
        peopleService.save(person);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/people/{id}")
    public PersonDTO getPerson(@PathVariable("id") int id) {
        return converter.convertToPersonDTO(peopleService.findOne(id));
    }

    @GetMapping("/books")
    public List<BookDTO> getBooks() {
        return booksService.findAll().stream()
                .map(converter::convertToBookDTO)
                .collect(Collectors.toList());
    }

    @PostMapping("/books")
    public ResponseEntity<HttpStatus> saveBook(
            @RequestBody @Valid BookDTO bookDTO,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            StringBuilder errorMessage = new StringBuilder();
            for (FieldError error: errors) {
                errorMessage.append(error.getField()).append(" - ")
                        .append(error.getDefaultMessage()).append("; ");
            }
            throw new BookNotCreatedException(errorMessage.toString());
        }

        Book book = converter.convertToBook(bookDTO);
        booksService.save(book);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/books/{id}")
    public BookDTO getBook(@PathVariable("id") int id) {
        return converter.convertToBookDTO(booksService.findOne(id));
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(PersonNotFoundException e) {
        ErrorResponse response = new ErrorResponse(
                "Человек с таким id не найден", System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(PersonNotCreatedException e) {
        ErrorResponse response = new ErrorResponse(
                e.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(BookNotFoundException e) {
        ErrorResponse response = new ErrorResponse(
                "Книга с таким id не найдена", System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(BookNotCreatedException e) {
        ErrorResponse response = new ErrorResponse(
                e.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
