package models;

import java.util.HashMap;
import java.util.Map;

public class User {
    private String uID;
    private String name;
    private String email;
    private String photoURL;

    public User() {
    }

    public User(String uID, String name, String email, String photoURL) {
        this.uID = uID;
        this.name = name;
        this.email = email;
        this.photoURL = photoURL;
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

    public Map<String, Object> dbModel(){
        Map<String, Object> map = new HashMap<>();
        map.put("uID", this.uID);
        map.put("name", this.name);
        map.put("email", this.email);
        map.put("photoURL", this.photoURL);
        return map;
    }

    @Override
    public String toString() {
        return "User{" +
                "uID='" + uID + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", photoURL='" + photoURL + '\'' +
                '}';
    }
}
