package com.example.camerastore;

public class UserHelperClass {
    String username,password,email,phoneNp;

    public UserHelperClass(){}
    public UserHelperClass(String username, String password, String email, String phoneNp) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNp = phoneNp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNp() { return phoneNp; }

    public void setPhoneNp(String phoneNp) { this.phoneNp = phoneNp; }
}
