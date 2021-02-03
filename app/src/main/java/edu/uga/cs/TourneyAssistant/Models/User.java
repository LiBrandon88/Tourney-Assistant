package edu.uga.cs.TourneyAssistant.Models;

public class User {
    private String name;
    private String email;
    private boolean td;

    public User() {
        //default constructor
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isTd() {
        return td;
    }

    public void setTd(boolean td) {
        this.td = td;
    }

    public User (String name, String email, boolean td){
        this.name = name;
        this.email = email;
        this.td = td;
    }
}
