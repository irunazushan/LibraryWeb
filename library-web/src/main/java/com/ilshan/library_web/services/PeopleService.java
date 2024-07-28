package com.ilshan.library_web.services;

import com.ilshan.library_web.models.Book;
import com.ilshan.library_web.models.Person;
import com.ilshan.library_web.repositories.BooksRepository;
import com.ilshan.library_web.repositories.PeopleRepository;
import com.ilshan.library_web.util.PersonNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {

    private final PeopleRepository peopleRepository;
    private final BooksRepository booksRepository;

    public PeopleService(PeopleRepository peopleRepository, BooksRepository booksRepository) {
        this.peopleRepository = peopleRepository;
        this.booksRepository = booksRepository;
    }

    public List<Person> findAll() {
        return peopleRepository.findAll();
    }

    public Person findOne(int id) {
        Optional<Person> foundPerson = peopleRepository.findById(id);
        return foundPerson.orElseThrow(PersonNotFoundException::new);
    }

    @Transactional
    public void save(Person person) {
        person.setCretedAt(new Date());
        peopleRepository.save(person);
    }

    @Transactional
    public void update(int id, Person updatedPerson) {
        updatedPerson.setId(id);
        updatedPerson.setUpdatedAt(new Date());
        peopleRepository.save(updatedPerson);
    }

    @Transactional
    public void delete(int id) {
        peopleRepository.deleteById(id);
    }

    public Optional<Person> findByFullName(String fullname) {
        return peopleRepository.findByFullName(fullname);
    }

    @Transactional
    public List<Book> getBooks(int id) {
        Person person = peopleRepository.getReferenceById(id);
        List<Book> books = person.getBooks();
        checkBooksExpiration(books);
        return books;
    }

    public void checkBooksExpiration(List<Book> books) {
        books.stream()
                .filter(this::isBookExpired)
                .forEach(book -> book.setExpired(true));
    }

    public boolean isBookExpired(Book book) {
        Date now = new Date();
        Date ownedTime = book.getOwnedTime();
        int passedTimeInDays = (int) ((now.getTime() - ownedTime.getTime())
                / (1000 * 60 * 60 * 24));
        return passedTimeInDays >= 10;
    }
}
