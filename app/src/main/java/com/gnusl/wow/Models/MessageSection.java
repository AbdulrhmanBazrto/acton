package com.gnusl.wow.Models;

public class MessageSection {

    private int imageResource;
    private String Content;

    public MessageSection(int imageResource, String content) {
        this.imageResource = imageResource;
        Content = content;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
}
