package com.example.demo.models;

public class User {
    private Integer id;
    private String Username;
    private String Password;
    private String Email;
    private String CardNumber;

    public User(Integer id, String username, String password, String email, String cardNumber) {
        this.id = id;
        Username = username;
        Password = password;
        Email = email;
        CardNumber = cardNumber;
    }

    public String getCardNumber() {
        return CardNumber;
    }

    public void setCardNumber(String cardNumber) {
        CardNumber = cardNumber;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
