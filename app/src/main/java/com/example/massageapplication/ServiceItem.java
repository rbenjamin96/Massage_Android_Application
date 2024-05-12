package com.example.massageapplication;

public class ServiceItem {
    private String name;
    private String info;
    private String price;
    private float rating;
    private final int imageResource;

    public ServiceItem(String name, String info, String price, float rating, int imageResource) {
        this.name = name;
        this.info = info;
        this.price = price;
        this.rating = rating;
        this.imageResource = imageResource;
    }

    public String getName() {
        return name;
    }

    public String getInfo() {
        return info;
    }

    public String getPrice() {
        return price;
    }

    public float getRating() {
        return rating;
    }
    public int getImageResource() {return imageResource;}
}
