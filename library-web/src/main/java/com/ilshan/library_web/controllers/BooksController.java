package com.ilshan.library_web.controllers;

import com.ilshan.library_web.models.Book;
import com.ilshan.library_web.models.Person;
import com.ilshan.library_web.services.BooksService;
import com.ilshan.library_web.services.PeopleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BooksController {

    private final BooksService booksService;
    private final PeopleService peopleService;

    @Autowired
    public BooksController(BooksService booksService, PeopleService peopleService) {
        this.booksService = booksService;

        this.peopleService = peopleService;
    }

    @GetMapping()
    public String index(@RequestParam Optional<Integer> page,
                        @RequestParam Optional<Integer> books_per_page,
                        @RequestParam Optional<Boolean> sort_by_year,
                        Model model) {

        model.addAttribute("books", booksService.findPage(
                page.orElse(0),
                books_per_page.orElse(booksService.findAll().size()),
                sort_by_year.orElse(false)));
        return "books/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        model.addAttribute("book", booksService.findOne(id));
        model.addAttribute("person", new Person());
        model.addAttribute("people", peopleService.findAll());
        boolean isFree = booksService.isFree(id);
        model.addAttribute("bookIsFree", isFree);
        if (!isFree) {
            model.addAttribute("assignedPerson", booksService.findOne(id).getOwner());
        }
        return "books/show";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "books/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "books/new";
        }
        booksService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String editPage(@PathVariable("id") int id, Model model) {
        model.addAttribute("book", booksService.findOne(id));
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") int id, @ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "books/new";
        }
        booksService.update(id, book);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/assign")
    public String assign(@PathVariable("id") int id,
                         @ModelAttribute("person") Person person) {
        booksService.assignBook(id, person);
        return "redirect:/books/{id}";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id, Model model) {

        Book book = booksService.findOne(id);
        if (!booksService.isFree(id)) {
            model.addAttribute("errorMessage", "Книга не может быть удалена пока не возвращена");
            model.addAttribute("book", booksService.findOne(id));
            model.addAttribute("person", new Person());
            model.addAttribute("people", peopleService.findAll());
            boolean isFree = booksService.isFree(id);
            model.addAttribute("bookIsFree", isFree);
            if (!isFree) {
                model.addAttribute("assignedPerson", booksService.findOne(id).getOwner());
            }
            return "books/show";
        }

        booksService.delete(id);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}/make_free")
    public String assign(@PathVariable("id") int id) {
        booksService.freeBook(id);
        return "redirect:/books/{id}";
    }

    @GetMapping("/search")
    public String searchPage(@RequestParam Optional<String> nameForSearch, Model model) {
        model.addAttribute("nameForSearch", nameForSearch.orElse(""));
        if (nameForSearch.isPresent()) {
            List<Book> books = booksService.findBooksWithName(nameForSearch.get());
            model.addAttribute("books", books);
        } else {
            model.addAttribute("books", Collections.emptyList());
        }
        return "books/search";
    }
}
