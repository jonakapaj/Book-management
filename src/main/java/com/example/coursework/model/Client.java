package com.example.coursework.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public final class Client extends User {
    //Client has name, surname, id, login, psw, email
    private LocalDate birthDate;
    private String address;
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Publication> myPublications;

    public Client(String name, String surname, String login, String password, String email, LocalDate birthDate, String address) {
        super(name, surname, login, password, email);
        this.birthDate = birthDate;
        this.address = address;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
