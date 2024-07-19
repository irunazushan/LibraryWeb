package com.ilshan.controllers;

import com.ilshan.dao.BookDAO;
import com.ilshan.dao.PersonDAO;
import com.ilshan.models.Book;
import com.ilshan.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/books")
public class BooksController {
    private final BookDAO bookDAO;
    private final PersonDAO personDAO;

    @Autowired
    public BooksController(BookDAO bookDAO, PersonDAO personDAO) {
        this.bookDAO = bookDAO;
        this.personDAO = personDAO;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("books", bookDAO.index());
        return "books/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id,Model model) {
        model.addAttribute("book", bookDAO.show(id));
        model.addAttribute("person", new Person());
        model.addAttribute("people", personDAO.index());
        boolean isFree = bookDAO.isFree(id);
        model.addAttribute("bookIsFree", bookDAO.isFree(id));
        if (!isFree) {
            model.addAttribute("assignedPerson", bookDAO.getAssignedPerson(id));
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
        bookDAO.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String editPage(@PathVariable("id") int id, Model model) {
        model.addAttribute("book", bookDAO.show(id));
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") int id, @ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "books/new";
        }
        bookDAO.update(id, book);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/assign")
    public String assign(@PathVariable("id") int id,
                         @ModelAttribute("person") Person person) {
        bookDAO.assignBook(id, person.getId());
        return "redirect:/books/{id}";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id, Model model) {

        Book book = bookDAO.show(id);
        if (!bookDAO.isFree(id)) {
            model.addAttribute("errorMessage", "Книга не может быть удалена пока не возвращена");
            model.addAttribute("book", bookDAO.show(id));
            model.addAttribute("person", new Person());
            model.addAttribute("people", personDAO.index());
            boolean isFree = bookDAO.isFree(id);
            model.addAttribute("bookIsFree", bookDAO.isFree(id));
            if (!isFree) {
                model.addAttribute("assignedPerson", bookDAO.getAssignedPerson(id));
            }
            return "books/show";
        }

        bookDAO.delete(id);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}/make_free")
    public String assign(@PathVariable("id") int id) {
        bookDAO.freeBook(id);
        return "redirect:/books/{id}";
    }
}
