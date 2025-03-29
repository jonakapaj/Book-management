package com.example.coursework.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public final class Admin extends User{
    private LocalDate employmentDate;
    private String phoneNum;

    public Admin(String name, String surname, String login, String password, String email, LocalDate employmentDate, String phoneNum) {
        super(name, surname, login, password, email);
        this.employmentDate = employmentDate;
        this.phoneNum = phoneNum;
    }
}
