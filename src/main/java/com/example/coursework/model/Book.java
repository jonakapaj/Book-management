package com.example.coursework.model;

import com.example.coursework.model.enums.Format;
import com.example.coursework.model.enums.Genre;
import com.example.coursework.model.enums.Language;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Book extends Publication {
    @Enumerated
    private Genre genre;
    @Enumerated
    private Language language;
    private String isbn;
    private String publisher;
    private int year;
    private int edition;

    public Book(String title, String description, LocalDateTime dateCreated, String author, Genre genre, int edition) {
        super(title, description, dateCreated, author);
        this.genre = genre;
        this.edition = edition;
    }

    public Book(String title, String description, String author, Format format, Genre genre, Language language, String isbn, String publisher, int year, int edition) {
        super(title, description, author, format);
        this.genre = genre;
        this.language = language;
        this.isbn = isbn;
        this.publisher = publisher;
        this.year = year;
        this.edition = edition;
    }

    public Book(String title, String description, String author, Format format, Genre genre, Language language, String isbn, String publisher) {
        super(title, description, author, format);
        this.genre = genre;
        this.language = language;
        this.isbn = isbn;
        this.publisher = publisher;
    }

    public Book(String title, String description, LocalDateTime dateCreated, String author, Genre genre, Language language, String isbn, String publisher, int year, int edition) {
        super(title, description, dateCreated, author);
        this.genre = genre;
        this.language = language;
        this.isbn = isbn;
        this.publisher = publisher;
        this.year = year;
        this.edition = edition;
    }

}
