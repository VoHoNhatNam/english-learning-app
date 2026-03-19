package com.example.englishlearningapp.data.model;

public class User {

<<<<<<< HEAD
    private String id;
    private String name;
    private String email;
    private boolean isVip;

=======
    private String email;
    private String password;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
>>>>>>> 8b0d1df9f835a4e91f8ca5c5352c54652cc8fe83
}