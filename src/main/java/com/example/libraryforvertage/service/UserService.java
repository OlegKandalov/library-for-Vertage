package com.example.libraryforvertage.service;

import com.example.libraryforvertage.dao.BookRepository;
import com.example.libraryforvertage.dao.UserRepository;
import com.example.libraryforvertage.entity.Book;
import com.example.libraryforvertage.entity.User;
import com.example.libraryforvertage.service.interfaces.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements CrudService<User> {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final BookService bookService;

    @Autowired
    public UserService(UserRepository userRepository, BookRepository bookRepository, BookService bookService) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.bookService = bookService;
    }

    @Override
    public User create(User user) {
        return userRepository.save(user);
    }

    @Override
    public User getById(Long id) {
        return userRepository.findById(id).orElseThrow();
    }

    @Override
    public void update(User user, Long id) {
        User userFromDB = userRepository.findById(id).orElseThrow();
        userFromDB.setName(user.getName());
        userFromDB.setBooks(user.getBooks());
        userRepository.save(userFromDB);
    }

    @Override
    public void remove(Long id) {
        userRepository.deleteById(id);
    }

    public String takeBook(Long user_id, Long book_id) {
        Book book = bookRepository.findById(book_id).orElseThrow();
        if (book.isFree() == true) {
            User user = userRepository.findById(user_id).orElseThrow();
            List<Book> usersBook = user.getBooks();
            usersBook.add(book);
            user.setBooks(usersBook);
            update(user, user_id);
            book.setFree(false);
            bookService.update(book, book_id);
            return "you received book";
        } else {
            return "this book reserved";
        }
    }

    public void returnBook(Long user_id, Long book_id) {
        Book book = bookRepository.findById(book_id).orElseThrow();
        User user = userRepository.findById(user_id).orElseThrow();
        book.setFree(true);
        book.setUser(null);
        user.getBooks().remove(book);
        bookService.update(book, book_id);
        update(user, user_id);
    }

    public List<User> getAllUser() {
        return userRepository.findAll();
    }
}
