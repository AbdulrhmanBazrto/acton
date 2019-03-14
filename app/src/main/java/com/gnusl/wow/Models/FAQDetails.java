package com.gnusl.wow.Models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FAQDetails implements Serializable {

    private int id;
    private String question;
    private String answer;


    public static FAQDetails newInstance(JSONObject jsonObject) {
        FAQDetails faqDetails = new FAQDetails();
        faqDetails.setId(jsonObject.optInt("id"));
        faqDetails.setQuestion(jsonObject.optString("question"));
        faqDetails.setAnswer(jsonObject.optString("answer"));

        return faqDetails;
    }

    public static List<FAQDetails> parseArray(JSONArray jsonArray) {

        List<FAQDetails> faqDetails = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            faqDetails.add(newInstance(jsonArray.optJSONObject(i)));
        }

        return faqDetails;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
