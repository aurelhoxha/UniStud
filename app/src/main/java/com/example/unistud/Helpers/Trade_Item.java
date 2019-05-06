package com.example.unistud.Helpers;

public class Trade_Item {

    private String itemId;
    private String title;
    private String category;
    private String description;
    private String location;
    private String price;
    private String image;
    private String ItemCreatorId;


    public Trade_Item() {

    }

    public Trade_Item(String itemId,String title, String category, String description,String price,String image, String ItemCreatorId){

        this.itemId=itemId;
        this.title = title;
        this.category = category;
        this.description = description;
        this.price=price;
        this.image=image;
        this.ItemCreatorId= ItemCreatorId;

    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getItemCreatorId() {
        return ItemCreatorId;
    }

    public void setItemCreatorId(String itemCreatorId) {
        ItemCreatorId = itemCreatorId;
    }

}
