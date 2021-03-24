package com.example.libraryforvertage.controllers;

import com.example.libraryforvertage.entity.Book;
import com.example.libraryforvertage.entity.User;
import com.example.libraryforvertage.service.BookService;
import com.example.libraryforvertage.service.UserService;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@WebAppConfiguration
public class UserControllerTest {

    private final UserService userService;
    private final BookService bookService;

    protected final WebApplicationContext context;
    protected MockMvc mockMvc;

    protected static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);

    @Autowired
    public UserControllerTest(WebApplicationContext context, UserService userService, BookService bookService) {
        this.context = context;
        this.userService = userService;
        this.bookService = bookService;
    }

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
    }

    @Test
    public void shouldGetAllUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @Sql(scripts = {"/sqlFilesForTests/booksForControllerTest.sql"})
    public void shouldCreateNewUser() throws Exception {
        User user = new User();
        Book numberTheStars = bookService.getById(21L);
        List<Book> bookList = new ArrayList<>();
        bookList.add(numberTheStars);
        user.setId(3L);
        user.setName("Max");
        user.setBooks(bookList);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.writeValueAsString(user));

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Max"))
                .andReturn();
    }

    @Test
    @Sql(scripts = {"/sqlFilesForTests/usersForControllerTest.sql"})
    public void shouldGetUserById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/31"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Norman"));
    }

    @Test
    @Sql(scripts = {"/sqlFilesForTests/userForDeleteUserTest.sql"})
    public void shouldDeleteUserById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/32"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @Sql(scripts = {"/sqlFilesForTests/userForUpdateUserTest.sql"})
    public void shouldUpdateUser() throws Exception {
        User jim = new User();
        jim.setName("Jim");
        userService.update(jim, 33L);
        User actualUser = userService.getById(33L);
        Assertions.assertEquals("Jim", actualUser.getName());

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users/33")
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.writeValueAsString(jim));

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
    }

    @Test
    @Sql(scripts = {"/sqlFilesForTests/reservedBooks.sql"})
    public void takeReservedBook() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/users/take/61/51")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        String example = "this book reserved";
        String actual = userService.takeBook(61L, 51L);
        Assertions.assertEquals(example, actual);
    }

    @Test
    @Sql(scripts = {"/sqlFilesForTests/freeBooks.sql"})
    public void takeFreeBook() throws Exception {
        String example = "you received book";
        String actual = userService.takeBook(81L, 71L);
        Assertions.assertEquals(example, actual);
    }

    @Test
    @Sql(scripts = {"/sqlFilesForTests/returnBook.sql"})
    public void shouldReturnBook() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/users/take/45/35")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
    }
}

