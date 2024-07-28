package com.ilshan.library_web.rest;

import com.ilshan.library_web.dto.DTOConverters;
import com.ilshan.library_web.dto.PersonDTO;
import com.ilshan.library_web.models.Person;
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
@RequestMapping("/api/people")
public class PersonRestController {
    private final PeopleService peopleService;
    private final PersonValidator personValidator;
    private final DTOConverters converter;

    @Autowired
    public PersonRestController(PeopleService peopleService,
                        PersonValidator personValidator,
                        DTOConverters converter) {
        this.peopleService = peopleService;
        this.converter = converter;
        this.personValidator = personValidator;
    }

    @GetMapping
    public List<PersonDTO> getPeople() {
        return peopleService.findAll().stream()
                .map(converter::convertToPersonDTO)
                .collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void savePerson(
            @RequestBody @Valid PersonDTO personDTO,
            BindingResult bindingResult) {
        Person person = converter.convertToPerson(personDTO);
        checkValidation(person, bindingResult);
        person.setCreatedBy("anonymous");
        peopleService.save(person);
    }

    @GetMapping("/{id}")
    public PersonDTO getPerson(@PathVariable("id") int id) {
        return converter.convertToPersonDTO(peopleService.findOne(id));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updatePerson(@PathVariable("id") int id,
                         @RequestBody @Valid PersonDTO personDTO,
                         BindingResult bindingResult) {
        Person person = converter.convertToPerson(personDTO);
        checkValidation(person, bindingResult);
        peopleService.update(id, person);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") int id) {
        Person person = peopleService.findOne(id);
        peopleService.delete(id);
    }

    private void checkValidation(Person person, BindingResult bindingResult) {
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
}
