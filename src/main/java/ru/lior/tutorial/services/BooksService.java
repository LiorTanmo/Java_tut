package ru.lior.tutorial.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.lior.tutorial.models.Book;
import ru.lior.tutorial.models.Person;
import ru.lior.tutorial.repositories.BooksRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
public class BooksService {
    private final BooksRepository booksRepository;

    @Autowired
    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    public List<Book> findAll(boolean sortByYear) {
        return sortByYear ? booksRepository.findAll(Sort.by("year"))
                :booksRepository.findAll() ;
    }

    public List<Book> findAll(int booksPerPage, int page ,boolean sortByYear){
        return sortByYear ? booksRepository.findAll(PageRequest.of(page, booksPerPage, Sort.by("year"))).getContent()
                : booksRepository.findAll(PageRequest.of(page, booksPerPage)).getContent();
    }

    public Book findOne(int id) {
        Optional<Book> foundBook = booksRepository.findById(id);
        return foundBook.orElse(null);
    }

    public List<Book> search(String search){
        return booksRepository.findByNameStartingWith(search);
    }

    @Transactional
    public void save(Book book) {
        booksRepository.save(book);
    }

    @Transactional
    public void update(int id, Book updBook) {
        updBook.setId(id);
        updBook.setOwner(this.getOwner(id));
        booksRepository.save(updBook);
    }

    public Person getOwner(int id){
        return booksRepository.findById(id).map(Book::getOwner).orElse(null);
    }

    @Transactional
    public void delete(int id) {
        booksRepository.deleteById(id);
    }

    @Transactional
    public void setOwner(int id, Optional<Person> person) {
        booksRepository.findById(id).ifPresent(
                book -> {
                    book.setOwner(person.orElse(null));
                    book.setTakenAt(person.isPresent()? new Date() : null);
                }
        );
    }

}
