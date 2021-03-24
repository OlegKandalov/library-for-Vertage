package com.example.libraryforvertage.services;

import com.example.libraryforvertage.dao.BookRepository;
import com.example.libraryforvertage.dao.UserRepository;
import com.example.libraryforvertage.service.BookService;
import com.example.libraryforvertage.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.stereotype.Component;
import org.springframework.test.context.jdbc.Sql;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private BookService bookService;
    private BookRepository bookRepository;
    private UserService userService;

    @BeforeAll
    void setUp() {
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        userService = new UserService(userRepository, bookRepository, bookService);
    }

    @Test
    @Sql(scripts = {"/sqlFilesForTests/freeBooks.sql"})
    public void takeReservedBook() {
        /*User testUser = new User();
        testUser.setName("Barny");
        testUser.setId(81L);
        Book testBook = new Book();
        testBook.setFree(false);
        testBook.setId(91L);
        testBook.setTitle("Java");

        Mockito.when(userService.takeBook(81L, 91L)).thenCallRealMethod();*/
        String example = "this book reserved";
        String actual = userService.takeBook(81L, 71L);
        Assertions.assertEquals(example, actual);
    }
}
