package ru.lior.tutorial.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@Entity
@Table
public class Book {
    @Id
    @Column(name = "book_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    @NotEmpty(message = "Имя не может быть пустым")
    @Size(min = 2,max = 100,message = "Название должно быть 2-100 символов")
    private String name;

    @Column
    @NotEmpty(message = "Автор не может быть пустым")
    @Size(min = 2,max = 100,message = "Имя автора должно быть 2-100 символов")
    private String author;

    @Column
    @Min(value = 0, message = "Год должен быть положительным")
    private int year;
    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "person_id")
    private Person owner;

    @Transient
    private boolean isOverdue;

    public Book (String name, String author, int year) {
        this.name = name;
        this.author = author;
        this.year = year;
    }

    public Book(){}

    public int getId() {
        return id;
    }



    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public boolean isOverdue() {
        return isOverdue;
    }

    public void setOverdue(boolean overdue) {
        isOverdue = overdue;
    }
}
