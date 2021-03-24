package com.example.libraryforvertage.dao;

import com.example.libraryforvertage.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
