package com.example.libraryforvertage.dao;

import com.example.libraryforvertage.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
