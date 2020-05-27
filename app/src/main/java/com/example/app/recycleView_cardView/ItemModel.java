package com.example.app.recycleView_cardView;

public class ItemModel {

    private String image;
    private String description;
    private String price;

    public ItemModel(String image, String description, String price) {
        this.image = image;
        this.description = description;
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
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
