package com.gnusl.wow.Models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FAQ {

    private int id;
    private String image;
    //    private String name;
    private String imageUrl;
    private List<FAQDetails> details;

    public static FAQ newInstance(JSONObject jsonObject) {
        FAQ faq = new FAQ();

        faq.setId(jsonObject.optInt("id"));
        faq.setImage(jsonObject.optString("image"));
        faq.setImageUrl(jsonObject.optString("image_url"));
        faq.setDetails(FAQDetails.parseArray(jsonObject.optJSONArray("faq_details")));

        return faq;

    }

    public static List<FAQ> parseArray(JSONArray jsonArray) {

        List<FAQ> faqs = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            faqs.add(newInstance(jsonArray.optJSONObject(i)));
        }

        return faqs;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<FAQDetails> getDetails() {
        return details;
    }

    public void setDetails(List<FAQDetails> details) {
        this.details = details;
    }

//    public String getName() {
//        return name;
//    }

//    public void setName(String name) {
//        this.name = name;
//    }
}
