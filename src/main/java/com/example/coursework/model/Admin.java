package com.example.coursework.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.Period;

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
    
    /**
     * Checks if the admin's employment period is within the valid range (less than maxYears)
     * @param maxYears Maximum number of years an admin can be employed
     * @return true if the admin's employment is current, false otherwise
     */
    public boolean isEmploymentCurrent(int maxYears) {
        if (employmentDate == null) {
            return false;
        }
        Period period = Period.between(employmentDate, LocalDate.now());
        return period.getYears() < maxYears;
    }
    
    /**
     * Returns the employment duration in years and months
     * @return String representation of employment duration
     */
    public String getEmploymentDuration() {
        if (employmentDate == null) {
            return "Not specified";
        }
        Period period = Period.between(employmentDate, LocalDate.now());
        return String.format("%d years, %d months", period.getYears(), period.getMonths());
    }
    
    @Override
    public String toString() {
        return String.format("Admin: %s %s (Employed: %s)", 
                             getName(), getSurname(), getEmploymentDuration());
    }
}
