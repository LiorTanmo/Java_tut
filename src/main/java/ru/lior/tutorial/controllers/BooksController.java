package ru.lior.tutorial.controllers;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.lior.tutorial.dao.BookDAO;
import ru.lior.tutorial.dao.PersonDAO;
import ru.lior.tutorial.models.Book;
import ru.lior.tutorial.models.Person;

//TODO rewrite using JPA methods
@Controller
@RequestMapping("/books")
public class BooksController {
    private final BookDAO dao;
    private final PersonDAO personDAO;
    @Autowired
    public BooksController(BookDAO bookDAO, PersonDAO personDAO) {
        this.dao = bookDAO;
        this.personDAO = personDAO;
    }

    @GetMapping()
    public String index(Model model){
        model.addAttribute("books", dao.index());
        return "books/index";
    }

    @GetMapping("/{id}")
    public String show(Model model, @ModelAttribute("person") Person person, @PathVariable("id") int id){
        model.addAttribute("book", dao.show(id));
        if(dao.getOwner(id).isPresent()){
            model.addAttribute("owner", dao.getOwner(id).get());
        } else {
          model.addAttribute("people", personDAO.index());
        }
        return "books/show";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book){
        return "books/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "books/new";
        }
        dao.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id){
        model.addAttribute("book", dao.show(id));
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String update (@ModelAttribute("book") @Valid Book book,  BindingResult bindingResult,
                          @PathVariable("id") int id){
        if(bindingResult.hasErrors())
            return "books/edit";
        dao.update(id, book);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/assign")
    public String assign (@ModelAttribute("person") Person person, @PathVariable("id") int book_id){
        dao.assignBook(person.getPerson_id(), book_id);
        return "redirect:/books/{id}";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id){
        dao.delete(id);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}/assign")
    public String removeOwner(@PathVariable("id") int book_id){
        dao.assignBook(null, book_id);
        return "redirect:/books/{id}";
    }
}
