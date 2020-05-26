package models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User implements Parcelable {

    private String uID;
    private String name;
    private String email;
    private String photoURL;
    private double rating;
    private String number;
    private List<String> hired;

    public User() {
    }

    public User(String uID, String name, String email, String photoURL, double rating, String number) {
        this.uID = uID;
        this.name = name;
        this.email = email;
        this.photoURL = photoURL;
        this.rating = rating;
        this.number = number;
        this.hired = new ArrayList<>();
    }

    protected User(Parcel in) {
        uID = in.readString();
        name = in.readString();
        email = in.readString();
        photoURL = in.readString();
        rating = in.readDouble();
        number = in.readString();
      // hired = in.createStringArrayList();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

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

    public void setHiredServices(ArrayList<String> list) {
        this.hired = list;
    }

    public List<String> getHiredServices() {
        return hired;
    }

    public void addHiredService(String AID){ hired.add(AID); }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uID);
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(photoURL);
        dest.writeDouble(rating);
        dest.writeString(number);
//        dest.readStringList(hired);
    }
}
