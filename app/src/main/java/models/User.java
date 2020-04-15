package models;

import java.util.HashMap;
import java.util.Map;

public class User {

    private String uID;
    private String name;
    private String email;
    private String photoURL;
    private double rating;
    private String number;

    public User() {
    }

    public User(String uID, String name, String email, String photoURL, double rating, String number) {
        this.uID = uID;
        this.name = name;
        this.email = email;
        this.photoURL = photoURL;
        this.rating = rating;
        this.number = number;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getuID() {
        return uID;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "User{" +
                "uID='" + uID + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", photoURL='" + photoURL + '\'' +
                ", rating=" + rating +
                ", number='" + number + '\'' +
                '}';
    }
}
