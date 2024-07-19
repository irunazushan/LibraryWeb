package com.ilshan.dao;

import com.ilshan.models.Book;
import com.ilshan.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class PersonDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Person> index() {
        return jdbcTemplate.query("SELECT * FROM person", new BeanPropertyRowMapper<>(Person.class));
    }

    public Optional<Person> show(String fullName) {
        return jdbcTemplate.query("SELECT * FROM person WHERE fullname=?", new Object[]{fullName} , new BeanPropertyRowMapper<>(Person.class))
                .stream().findAny();
    }

    public Person show(int id) {
        return jdbcTemplate.query("SELECT * FROM person WHERE id=?", new Object[]{id} , new BeanPropertyRowMapper<>(Person.class))
                .stream().findAny().orElse(null);
    }

    public void save(Person person) {
        jdbcTemplate.update("INSERT INTO person(fullname, birth_date) VALUES (?, ?)", person.getFullName(), person.getBirthDate());
    }

    public void update(int id, Person updatedPerson) {
        jdbcTemplate.update("UPDATE person SET fullname=?, birth_date=? WHERE id=?", updatedPerson.getFullName(), updatedPerson.getBirthDate(), id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM person WHERE id=?", id);
    }

    public List<Book> giveBooks(int person_id) {
        return jdbcTemplate.query("SELECT * FROM book WHERE id IN (SELECT book_id FROM book_person WHERE person_id = ?)", new Object[]{person_id}, new BeanPropertyRowMapper<>(Book.class));
    }
}