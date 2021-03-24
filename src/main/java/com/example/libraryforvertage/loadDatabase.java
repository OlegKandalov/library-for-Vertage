package com.example.libraryforvertage;

import com.example.libraryforvertage.dao.BookRepository;
import com.example.libraryforvertage.dao.UserRepository;
import com.example.libraryforvertage.entity.Book;
import com.example.libraryforvertage.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class loadDatabase {

    private static final Logger log = LoggerFactory.getLogger(loadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository,
                                   BookRepository bookRepository) {
        return args -> {
            log.info("Preloading " + userRepository.save(new User("Misha")));
            log.info("Preloading " + userRepository.save(new User("Alex")));
            log.info("Preloading" + bookRepository.save(new Book("Harry Potter", false)));
            log.info("Preloading" + bookRepository.save(new Book("War and Peace")));
        };
    }
}
