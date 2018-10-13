package com.gnusl.wow.Models;

public class ChatMessage {

    private int imageResource;
    private String name;
    private String message;

    public ChatMessage(int imageResource, String name, String message) {
        this.imageResource = imageResource;
        this.name = name;
        this.message = message;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
