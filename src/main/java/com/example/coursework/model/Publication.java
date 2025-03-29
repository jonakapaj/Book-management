package com.example.coursework.model;

import com.example.coursework.model.enums.Format;
import com.example.coursework.model.enums.PublicationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public abstract class Publication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;
    protected String title;
    protected String description;
    protected LocalDateTime dateCreated;
    protected String author;
    @OneToMany(mappedBy = "publication", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    protected List<Comment> commentList;
    @Enumerated
    protected Format format;
    @Enumerated
    protected PublicationStatus publicationStatus;
    @ManyToOne
    protected Client owner;

    public Publication(String title, String description, String author, Format format, PublicationStatus publicationStatus) {
        this.title = title;
        this.description = description;
        this.author = author;
        this.format = format;
        this.publicationStatus = publicationStatus;
    }

    public Publication(String title, String description, String author, Format format) {
        this.title = title;
        this.description = description;
        this.author = author;
        this.format = format;
        this.publicationStatus = PublicationStatus.AVAILABLE;
    }

    public Publication(String title, String description, LocalDateTime dateCreated, String author) {
        this.title = title;
        this.description = description;
        this.dateCreated = dateCreated;
        this.author = author;
    }

    @Override
    public String toString() {
        return id + " " + title;
    }

}
