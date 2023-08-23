package ru.lior.tutorial.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.lior.tutorial.dao.BookDAO;
import ru.lior.tutorial.dao.PersonDAO;
import ru.lior.tutorial.models.Book;
import ru.lior.tutorial.models.Person;
import ru.lior.tutorial.util.PersonValidator;

import java.util.List;

//TODO ПЕРЕПИСАТЬ ИСПОЛЬЗУЯ JPA
@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PersonDAO dao;
    private final PersonValidator personValidator;

    @Autowired
    public PeopleController(PersonDAO dao, PersonValidator personValidator) {
        this.dao = dao;
        this.personValidator = personValidator;

    }

    @GetMapping()
    public String index(Model model){
        model.addAttribute("people", dao.index());
        return "people/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model){
        model.addAttribute("person", dao.show(id));
        model.addAttribute("personalBooks", dao.indexPersonBooks(id));
        return "people/show";
    }



    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person") Person person){
        return "people/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult){
        personValidator.validate(person, bindingResult);
        if(bindingResult.hasErrors()){
            return "people/new";
        }
        dao.save(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id){
        model.addAttribute("person", dao.show(id));
        return "people/edit";
    }

    @PatchMapping("/{id}")
    public String update (@ModelAttribute("person") @Valid Person person,  BindingResult bindingResult,
                          @PathVariable("id") int id){
        personValidator.validate(person, bindingResult);
        if(bindingResult.hasErrors())
            return "people/edit";
        dao.update(id, person);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id){
        dao.delete(id);
        return "redirect:/people";
    }
}
