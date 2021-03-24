package com.example.libraryforvertage.service;

import com.example.libraryforvertage.dao.BookRepository;
import com.example.libraryforvertage.entity.Book;
import com.example.libraryforvertage.exceptions.BadIdException;
import com.example.libraryforvertage.exceptions.BookNameIsNullException;
import com.example.libraryforvertage.exceptions.TooLongException;
import com.example.libraryforvertage.service.interfaces.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService implements CrudService<Book> {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book create(Book book) {
        book.setTitle(getValidatedBookName(book.getTitle()));
        return bookRepository.save(book);
    }

    @Override
    public Book getById(Long id) {
        if (id <= 0L) {
            throw new BadIdException();
        }
        return bookRepository.findById(id).orElseThrow();
    }

    @Override
    public void update(Book book, Long id) {
        Book bookFromDB = bookRepository.findById(id).orElseThrow();
        bookFromDB.setTitle(getValidatedBookName(book.getTitle()));
        bookFromDB.setUser(book.getUser());
        bookFromDB.setFree(book.isFree());
        bookRepository.save(bookFromDB);
    }

    @Override
    public void remove(Long id) {
        bookRepository.deleteById(id);
    }

    public String getValidatedBookName(final String name) {
        if (name == null) {
            throw new BookNameIsNullException();
        }
        if (name.length() > 20) {
            throw new TooLongException();
        }
        return name.trim();
    }
}
