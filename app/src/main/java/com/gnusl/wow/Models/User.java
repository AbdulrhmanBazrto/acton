package com.gnusl.wow.Models;

public class User {

    private int imageResource;
    private String name;

    public User(int imageResource) {
        this.imageResource = imageResource;
    }

    public User(int imageResource, String name) {
        this.imageResource = imageResource;
        this.name = name;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
