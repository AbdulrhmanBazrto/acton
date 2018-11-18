package com.gnusl.wow.Models;

public class RegisterParams {

    private String name;
    private String email;
    private String password;
    private String phone;
    private String image;
    private String type;
    private String type_ar;
    private String social_type;
    private String social_id;
    private String fcm_token;
    private String specialization_ids="";
    private String classroom_ids="";
    private String subject_ids="";
    private String lat="";
    private String lon="";

    public RegisterParams(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType_ar() {
        return type_ar;
    }

    public void setType_ar(String type_ar) {
        this.type_ar = type_ar;
    }

    public String getSpecialization_ids() {
        return specialization_ids;
    }

    public void setSpecialization_ids(String specialization_ids) {
        this.specialization_ids = specialization_ids;
    }

    public String getClassroom_ids() {
        return classroom_ids;
    }

    public void setClassroom_ids(String classroom_ids) {
        this.classroom_ids = classroom_ids;
    }

    public String getSubject_ids() {
        return subject_ids;
    }

    public void setSubject_ids(String subject_ids) {
        this.subject_ids = subject_ids;
    }

    public String getSocial_type() {
        return social_type;
    }

    public void setSocial_type(String social_type) {
        this.social_type = social_type;
    }

    public String getSocial_id() {
        return social_id;
    }

    public void setSocial_id(String social_id) {
        this.social_id = social_id;
    }

    public String getFcm_token() {
        return fcm_token;
    }

    public void setFcm_token(String fcm_token) {
        this.fcm_token = fcm_token;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

}
