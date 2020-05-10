package models;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Advert implements Parcelable {



    public enum Category {
        CARPENTRY(0), MECHANICS(1), TECHNOLOGY(2), COOKING(3), CHILD(4), PET(5), EVENT(6), HEALTH(7), OTHER(8);
        public int value;

        Category(int n){
            value = n;
        }

        public static int getValue(Category c){
            return c.value;
        }

        public static Category convert(String s){
            switch (s) {
                case "CARPENTRY":
                    return Category.CARPENTRY;
                case "MECHANICS":
                    return Category.MECHANICS;
                case "TECHNOLOGY":
                    return Category.TECHNOLOGY;
                case "COOKING":
                    return Category.COOKING;
                case "CHILD":
                    return Category.CHILD;
                case "PET":
                    return Category.PET;
                case "EVENT":
                    return Category.EVENT;
                case "HEALTH":
                    return Category.HEALTH;
                default:
                    return Category.OTHER;
            }
        }
    }


    private String id;
    private String ownerID;
    private Category category;
    private String description;
    private int price;
    private boolean hourly;
    private List<String> imagesURL;
    private int rating;
    private int voteCount;

    protected Advert(Parcel in) {
        id = in.readString();
        ownerID = in.readString();
        category = Category.valueOf(in.readString());
        description = in.readString();
        price = in.readInt();
        hourly = in.readByte() != 0;
        imagesURL = new ArrayList<>();
        in.readStringList(imagesURL);
        rating = in.readInt();
        voteCount = in.readInt();
    }

    public static final Creator<Advert> CREATOR = new Creator<Advert>() {
        @Override
        public Advert createFromParcel(Parcel in) {
            return new Advert(in);
        }

        @Override
        public Advert[] newArray(int size) {
            return new Advert[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(ownerID);
       dest.writeString(category.name());
        dest.writeString(description);
        dest.writeInt(price);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            dest.writeBoolean(hourly);
        }
        dest.writeStringList(imagesURL);
        dest.writeInt(rating);
        dest.writeInt(voteCount);
    }

    public Advert() {}

    public Advert(String id, String ownerID, Category category, String description, int price, boolean hourly, List<String> imagesURL, int rating, int voteCount) {
        this.id = id;
        this.ownerID = ownerID;
        this.category = category;
        this.description = description;
        this.price = price;
        this.hourly = hourly;
        this.imagesURL = imagesURL;
        this.rating = rating;
        this.voteCount = voteCount;
    }

    public String getId() {
        return id;
    }

    public Category getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }

    public boolean isHourly() {
        return hourly;
    }

    public List<String> getImagesURL() {
        return imagesURL;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setHourly(boolean hourly) {
        this.hourly = hourly;
    }

    public void setImagesURL(List<String> imagesURL) {
        this.imagesURL = imagesURL;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    @Override
    public String toString() {
        return "Advert{" +
                "id='" + id + '\'' +
                ", ownerID='" + ownerID + '\'' +
                ", category=" + category +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", hourly=" + hourly +
                ", imagesURL=" + imagesURL +
                ", rating=" + rating +
                ", voteCount=" + voteCount +
                '}';
    }
}
