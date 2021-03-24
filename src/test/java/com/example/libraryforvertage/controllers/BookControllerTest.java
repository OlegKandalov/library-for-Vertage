package com.example.libraryforvertage.controllers;

import com.example.libraryforvertage.entity.Book;
import com.example.libraryforvertage.service.BookService;
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

@SpringBootTest
@WebAppConfiguration
public class BookControllerTest {

    private final BookService bookService;

    protected final WebApplicationContext context;
    protected MockMvc mockMvc;
    protected static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);

    @Autowired
    public BookControllerTest(WebApplicationContext context, BookService bookService) {
        this.context = context;
        this.bookService = bookService;
    }

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
    }

    @Test
    public void shouldCreateNewBook() throws Exception {
        Book book = new Book();
        book.setId(41L);
        book.setTitle("Black Beauty");
        book.setFree(true);
        book.setUser(null);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/books/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.writeValueAsString(book));

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title")
                        .value("Black Beauty")).andReturn();
    }

    @Test
    @Sql(scripts = {"/sqlFilesForTests/booksForControllerTest.sql"})
    public void shouldGetBookById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/books/22"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title")
                        .value("The Chronicles of Narnia"));
    }

    @Test
    @Sql(scripts = {"/sqlFilesForTests/bookForDeleteBookTest.sql"})
    public void shouldDeleteBookById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/books/23"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @Sql(scripts = {"/sqlFilesForTests/bookForUpdateBookTest.sql"})
    public void shouldUpdateUser() throws Exception {
        Book northernLights = new Book();
        northernLights.setTitle("Northern Lights");
        bookService.update(northernLights, 24L);
        Book actualBook = bookService.getById(24L);
        Assertions.assertEquals("Northern Lights", actualBook.getTitle());

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/books/24")
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.writeValueAsString(northernLights));

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
    }


}
