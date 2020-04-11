package recycleView_cardView;

public class ItemModel {

    private int image;
    private String description;
    private String price;

    public ItemModel(int image, String description, String price) {
        this.image = image;
        this.description = description;
        this.price = price;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
