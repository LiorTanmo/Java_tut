package ru.lior.tutorial.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.List;


@Entity
@Table
public class Person {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int person_id;
    @Column
    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, max = 30, message = "Name should be between 2 and 30 characters")
    private String name;

    @Column
    @Min(value = 1900, message = "Введите действительный год рождения")
    private int year;

    @OneToMany(mappedBy = "owner")
    private List<Book> books;

    public Person() {
    }
    public Person(String name, int year) {
        this.name = name;
        this.year = year;
    }

    public int getPerson_id() {
        return person_id;
    }

    public void setPerson_id(int id) {
        this.person_id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
