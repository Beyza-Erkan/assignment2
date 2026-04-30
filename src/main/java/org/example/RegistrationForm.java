// Registration form validation logic for Create New Account page
package org.example;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class RegistrationForm {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String confirmPassword;
    private String dateOfBirth;

    public RegistrationForm(String firstName, String lastName,
                            String email, String password,
                            String confirmPassword, String dateOfBirth) {
        this.firstName       = firstName;
        this.lastName        = lastName;
        this.email           = email;
        this.password        = password;
        this.confirmPassword = confirmPassword;
        this.dateOfBirth     = dateOfBirth;
    }

    public String submit() {
        validateFirstName(firstName);
        validateLastName(lastName);
        validateEmail(email);
        validatePassword(password);
        validateConfirmPassword(password, confirmPassword);
        validateDateOfBirth(dateOfBirth);
        return "Registration successful!";
    }

    void validateFirstName(String name) {
        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("First name cannot be empty.");
        if (name.trim().length() < 2)
            throw new IllegalArgumentException("First name must be at least 2 characters.");
        if (name.trim().length() > 50)
            throw new IllegalArgumentException("First name must be at most 50 characters.");
        if (!name.matches("[a-zA-Z\\s\\-]+"))
            throw new IllegalArgumentException("First name contains invalid characters.");
    }

    void validateLastName(String name) {
        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Last name cannot be empty.");
        if (name.trim().length() < 2)
            throw new IllegalArgumentException("Last name must be at least 2 characters.");
        if (name.trim().length() > 50)
            throw new IllegalArgumentException("Last name must be at most 50 characters.");
        if (!name.matches("[a-zA-Z\\s\\-]+"))
            throw new IllegalArgumentException("Last name contains invalid characters.");
    }

    void validateEmail(String email) {
        if (email == null || email.trim().isEmpty())
            throw new IllegalArgumentException("Email cannot be empty.");
        if (email.contains(" "))
            throw new IllegalArgumentException("Email cannot contain spaces.");
        int atIndex = email.indexOf('@');
        if (atIndex < 1)
            throw new IllegalArgumentException("Email must contain '@' symbol.");
        if (email.indexOf('@', atIndex + 1) != -1)
            throw new IllegalArgumentException("Email must contain exactly one '@'.");
        String domain = email.substring(atIndex + 1);
        if (!domain.contains(".") || domain.startsWith(".") || domain.endsWith("."))
            throw new IllegalArgumentException("Email domain is invalid.");
    }

    void validatePassword(String password) {
        if (password == null || password.isEmpty())
            throw new IllegalArgumentException("Password cannot be empty.");
        if (password.length() < 8)
            throw new IllegalArgumentException("Password must be at least 8 characters.");
        if (password.length() > 64)
            throw new IllegalArgumentException("Password must be at most 64 characters.");
        if (!password.matches(".*[A-Z].*"))
            throw new IllegalArgumentException("Password must contain at least one uppercase letter.");
        if (!password.matches(".*[0-9].*"))
            throw new IllegalArgumentException("Password must contain at least one digit.");
        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*"))
            throw new IllegalArgumentException("Password must contain at least one special character.");
    }

    void validateConfirmPassword(String password, String confirmPassword) {
        if (confirmPassword == null)
            throw new IllegalArgumentException("Please confirm your password.");
        if (!password.equals(confirmPassword))
            throw new IllegalArgumentException("Passwords do not match.");
    }

    void validateDateOfBirth(String dob) {
        if (dob == null || dob.trim().isEmpty())
            throw new IllegalArgumentException("Date of birth cannot be empty.");
        LocalDate birthDate;
        try {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            birthDate = LocalDate.parse(dob.trim(), fmt);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Date of birth must be in dd/MM/yyyy format.");
        }
        int age = Period.between(birthDate, LocalDate.now()).getYears();
        if (age < 13)
            throw new IllegalArgumentException("You must be at least 13 years old.");
        if (age > 120)
            throw new IllegalArgumentException("Please enter a valid date of birth.");
    }

    public String getFirstName()       { return firstName; }
    public String getLastName()        { return lastName; }
    public String getEmail()           { return email; }
    public String getPassword()        { return password; }
    public String getConfirmPassword() { return confirmPassword; }
    public String getDateOfBirth()     { return dateOfBirth; }
}