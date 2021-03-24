package com.example.libraryforvertage.services;

import com.example.libraryforvertage.dao.BookRepository;
import com.example.libraryforvertage.exceptions.BadIdException;
import com.example.libraryforvertage.exceptions.BookNameIsNullException;
import com.example.libraryforvertage.exceptions.TooLongException;
import com.example.libraryforvertage.service.BookService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    private BookService bookService;

    @BeforeAll
    void setUp() {
        BookRepository bookRepository = Mockito.mock(BookRepository.class);
        bookService = new BookService(bookRepository);
    }

    @Test
    void getBookByIdBadIdExceptionTest() {
        Assertions.assertThrows(
                BadIdException.class, () -> bookService.getById(-1L)
        );
    }

    @Test
    void shouldReturnBookNameIsNullException() {
        Assertions.assertThrows(
                BookNameIsNullException.class, () -> bookService.getValidatedBookName(null)
        );
    }

    @Test
    void shouldReturnTooLongException() {
        String tooLongTitle =
                "The Marvelous Land of Oz";
        Assertions.assertThrows(
                TooLongException.class, () -> bookService.getValidatedBookName(tooLongTitle)
        );
    }

    @Test
    void shouldRemoveSpaces() {
        String example = "trim";
        String stringWithSpaces = "    trim    ";
        String actual = bookService.getValidatedBookName(stringWithSpaces);
        Assertions.assertEquals(example, actual);
    }
}
