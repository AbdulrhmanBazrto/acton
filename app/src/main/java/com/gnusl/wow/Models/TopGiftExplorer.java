package com.gnusl.wow.Models;

import java.util.ArrayList;

/**
 * Created by Yehia on 9/29/2018.
 */

public class TopGiftExplorer {

    private int backroundResource;
    private int imageResource;
    private String content;
    private int[] people;

    public TopGiftExplorer(){}

    public TopGiftExplorer(int backroundResource, int imageResource, String content, int[] people) {
        this.backroundResource = backroundResource;
        this.imageResource = imageResource;
        this.content = content;
        this.people = people;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int[] getPeople() {
        return people;
    }

    public void setPeople(int[] people) {
        this.people = people;
    }

    public int getBackroundResource() {
        return backroundResource;
    }

    public void setBackroundResource(int backroundResource) {
        this.backroundResource = backroundResource;
    }
}
