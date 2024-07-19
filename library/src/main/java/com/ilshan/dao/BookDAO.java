package com.ilshan.dao;

import com.ilshan.models.Book;
import com.ilshan.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BookDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Book> index() {
        return jdbcTemplate.query("SELECT * FROM book", new BeanPropertyRowMapper<>(Book.class));
    }

    public Book show(int id) {
        return jdbcTemplate.query("SELECT * FROM book WHERE id=?", new Object[]{id} , new BeanPropertyRowMapper<>(Book.class))
                .stream().findAny().orElse(null);
    }

    public void save(Book book) {
        jdbcTemplate.update("INSERT INTO book(name, author, year) VALUES (?, ?, ?)", book.getName(), book.getAuthor(), book.getYear());
    }

    public void update(int id, Book updatedBook) {
        jdbcTemplate.update("UPDATE book SET name=?, author=?, year=? WHERE id=?", updatedBook.getName(), updatedBook.getAuthor(), updatedBook.getYear(), id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM book WHERE id=?", id);
    }

    public void assignBook(int book_id, int person_id) {
        jdbcTemplate.update("INSERT INTO book_person(book_id, person_id) VALUES (?, ?)", book_id, person_id);
    }

    public boolean isFree(int book_id) {
        return Boolean.FALSE.equals(jdbcTemplate.queryForObject("SELECT EXISTS (SELECT * FROM book_person WHERE book_id=?)", new Object[]{book_id}, Boolean.class));
    }

    public Person getAssignedPerson(int book_id) {
        return jdbcTemplate.query("SELECT * FROM person WHERE id = (SELECT person_id FROM book_person WHERE book_id=?)", new Object[]{book_id} , new BeanPropertyRowMapper<>(Person.class))
                .stream().findAny().orElse(null);
    }

    public void freeBook(int book_id) {
        jdbcTemplate.update("DELETE FROM book_person WHERE book_id=?", book_id);
    }

}
