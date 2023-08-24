package ru.lior.tutorial.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.lior.tutorial.models.Book;

import java.util.Optional;

@Repository
public interface BooksRepository extends JpaRepository<Book, Integer> {
    Optional<Book> findByNameStartingWith(String input);
}