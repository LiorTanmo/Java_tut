package ru.lior.tutorial.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.lior.tutorial.models.Book;
import ru.lior.tutorial.models.Person;
import ru.lior.tutorial.repositories.BooksRepository;

import java.util.List;


@Service
@Transactional(readOnly = true)
public class BooksService {
    private final BooksRepository booksRepository;

    @Autowired
    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }


    public List<Book> findByOwner(Person owner) {
        return booksRepository.findByOwner(owner);
    }
}
