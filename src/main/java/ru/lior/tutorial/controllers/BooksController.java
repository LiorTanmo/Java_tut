package ru.lior.tutorial.controllers;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.lior.tutorial.models.Book;
import ru.lior.tutorial.models.Person;
import ru.lior.tutorial.services.BooksService;
import ru.lior.tutorial.services.PeopleService;
import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BooksController {

    private final BooksService booksService;
    private final PeopleService peopleService;


    @Autowired
    public BooksController(BooksService booksService, PeopleService peopleService) {
        this.booksService = booksService;
        this.peopleService = peopleService;
    }

    @GetMapping()
    public String index(Model model, @RequestParam(required = false) Integer books_per_page,
                        @RequestParam(required = false) Integer page,
                        @RequestParam(required = false) boolean sortByYear){
        if (page != null && books_per_page != null)
             model.addAttribute("books", booksService.findAll(books_per_page, page, sortByYear));
        else model.addAttribute("books", booksService.findAll(sortByYear));
        return "books/index";
    }


    @GetMapping("/search")
    public String search(){
        return "books/search";
    }

    @PostMapping("/search")
    public String search(Model model, @RequestParam(required = false) String input){
        model.addAttribute("books", booksService.search(input));
        return "books/search";
    }

    @GetMapping("/{id}")
    public String show(Model model, @ModelAttribute("person") Person person, @PathVariable("id") int id){
        model.addAttribute("book", booksService.findOne(id));
        if(booksService.findOne(id).getOwner() != null){
            model.addAttribute("owner", booksService.getOwner(id));
        } else {
          model.addAttribute("people", peopleService.findAll());
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
        booksService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id){
        model.addAttribute("book", booksService.findOne(id));
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String update (@ModelAttribute("book") @Valid Book book,  BindingResult bindingResult,
                          @PathVariable("id") int id){
        if(bindingResult.hasErrors())
            return "books/edit";
        booksService.update(id, book);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/assign")
    public String assign (@ModelAttribute("person") Person person, @PathVariable("id") int book_id){
        booksService.setOwner(book_id, Optional.ofNullable(person));
        return "redirect:/books/{id}";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id){
        booksService.delete(id);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}/assign")
    public String removeOwner(@PathVariable("id") int book_id){
        booksService.setOwner(book_id, Optional.empty());
        return "redirect:/books/{id}";
    }
}
