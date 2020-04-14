package models;

public class Advert {

    public enum Category {
        CARPENTRY, MECHANICS, TECHNOLOGY, COOKING, CHILD, PET, EVENT, HEALTH, OTHER;

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
    private Category category;
    private String description;
    private int price;
    private boolean hourly;
    private String[] imagesURL;

    public Advert() {}

    public Advert(String id, Category category, String description, int price, boolean hourly, String[] imagesURL) {
        this.id = id;
        this.category = category;
        this.description = description;
        this.price = price;
        this.hourly = hourly;
        this.imagesURL = imagesURL;
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

    public String[] getImagesURL() {
        return imagesURL;
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

    public void setImagesURL(String[] imagesURL) {
        this.imagesURL = imagesURL;
    }
}
