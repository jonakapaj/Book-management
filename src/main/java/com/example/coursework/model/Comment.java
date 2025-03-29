package com.example.coursework.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String message;
    private LocalDateTime dateCreated;
    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Comment> replies;
    @ManyToOne
    private Comment parentComment;
    private LocalDateTime dateModified;
    @Transient
    private Client owner;
    @ManyToOne
    private Publication publication;
    @ManyToOne
    private Chat chat;

    public Comment(String title, String message, Client owner) {
        this.title = title;
        this.message = message;
        this.owner = owner;
        this.dateCreated = LocalDateTime.now();
        this.replies = new ArrayList<>();
    }
}

