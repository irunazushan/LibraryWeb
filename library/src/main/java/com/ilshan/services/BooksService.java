package com.ilshan.services;

import com.ilshan.models.Book;
import com.ilshan.models.Person;
import com.ilshan.repositories.BooksRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BooksService {

    private final BooksRepository booksRepository;

    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    public List<Book> findAll() {
        return booksRepository.findAll();
    }

    public List<Book> findPage(int page, int books_per_page, boolean sort_by_year) {
        List<Book> books;
        if (sort_by_year)
            books = booksRepository.findAll(PageRequest.of(
                    page, books_per_page, Sort.by("year"))).getContent();
        else
            books = booksRepository.findAll(PageRequest.of(
                    page, books_per_page)).getContent();
        return books;
    }

    public Book findOne(int id) {
        Optional<Book> foundBook = booksRepository.findById(id);
        return foundBook.orElse(null);
    }

    @Transactional
    public void save(Book book) {
        booksRepository.save(book);
    }

    @Transactional
    public void update(int id, Book updatedBook) {
        Book bookToBeUpdated = booksRepository.findById(id).get();
        updatedBook.setId(id);
        updatedBook.setOwner(bookToBeUpdated.getOwner());
        updatedBook.setOwnedTime(bookToBeUpdated.getOwnedTime());
        booksRepository.save(updatedBook);
    }

    @Transactional
    public void delete(int id) {
        booksRepository.deleteById(id);
    }

    @Transactional
    public void assignBook(int book_id, Person person) {
        Book book = booksRepository.getOne(book_id);
        book.setOwnedTime(new Date());
        book.setExpired(false);
        book.setOwner(person);
    }

    public boolean isFree(int id) {
        return booksRepository.getOne(id).getOwner() == null;
    }

    @Transactional
    public void freeBook(int id) {
        Book book = booksRepository.getOne(id);
        book.setOwnedTime(null);
        book.setExpired(false);
        book.getOwner().getBooks().remove(book);
        book.setOwner(null);
    }

    public List<Book> findBooksWithName(String bookName) {
        return booksRepository.findByNameStartingWith(bookName);
    }

}
