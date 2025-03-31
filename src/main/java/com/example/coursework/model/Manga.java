package com.example.coursework.model;


import com.example.coursework.model.enums.Language;
import com.example.coursework.model.enums.MangaGenre;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Manga extends Publication implements Serializable {
    private MangaGenre mangaGenre;
    private Language originalLanguage;
    private boolean color;
    private String illustrator;
    private int volume;

    public Manga(String title, String description, LocalDateTime dateCreated, String author, MangaGenre mangaGenre, Language originalLanguage, boolean color, String illustrator, int volume) {
        super(title, description, dateCreated, author);
        this.mangaGenre = mangaGenre;
        this.originalLanguage = originalLanguage;
        this.color = color;
        this.illustrator = illustrator;
        this.volume = volume;
    }

    public void setCreationDate(LocalDateTime now) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setCreationDate'");
    }


}
