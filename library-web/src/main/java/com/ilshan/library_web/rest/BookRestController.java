package com.ilshan.library_web.rest;

import com.ilshan.library_web.dto.BookDTO;
import com.ilshan.library_web.dto.DTOConverters;
import com.ilshan.library_web.models.Book;
import com.ilshan.library_web.services.BooksService;
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
@RequestMapping("/api/books")
public class BookRestController {
    private final BooksService booksService;
    private final DTOConverters converter;

    @Autowired
    public BookRestController( BooksService booksService, DTOConverters converter) {
        this.booksService = booksService;
        this.converter = converter;
    }

    @GetMapping
    public List<BookDTO> getBooks() {
        return booksService.findAll().stream()
                .map(converter::convertToBookDTO)
                .collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void saveBook(
            @RequestBody @Valid BookDTO bookDTO,
            BindingResult bindingResult) {
        checkValidation(bindingResult);
        Book book = converter.convertToBook(bookDTO);
        booksService.save(book);
    }

    @GetMapping("/{id}")
    public BookDTO getBook(@PathVariable("id") int id) {
        return converter.convertToBookDTO(booksService.findOne(id));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updatePerson(@PathVariable("id") int id,
                             @RequestBody @Valid BookDTO bookDTO,
                             BindingResult bindingResult) {
        checkValidation(bindingResult);
        Book book = converter.convertToBook(bookDTO);
        booksService.update(id, book);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") int id) {
        Book book = booksService.findOne(id);
        booksService.delete(id);
    }

    @ExceptionHandler(BookNotFoundException.class)
    private ResponseEntity<ErrorResponse> handleException(BookNotFoundException e) {
        ErrorResponse response = new ErrorResponse(
                "Книга с таким id не найдена", System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BookNotCreatedException.class)
    private ResponseEntity<ErrorResponse> handleException(BookNotCreatedException e) {
        ErrorResponse response = new ErrorResponse(
                e.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private void checkValidation(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            StringBuilder errorMessage = new StringBuilder();
            for (FieldError error : errors) {
                errorMessage.append(error.getField()).append(" - ")
                        .append(error.getDefaultMessage()).append("; ");
            }
            throw new BookNotCreatedException(errorMessage.toString());
        }
    }
}
