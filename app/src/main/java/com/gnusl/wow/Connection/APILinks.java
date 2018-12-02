package com.gnusl.wow.Connection;

public enum APILinks {

    Base_API_Url("http://wow.gnusl.com/api/"),

    Register_Url(Base_API_Url.getLink()+"register"),
    Login_Url(Base_API_Url.getLink()+"login"),
    Login_Social_Url(Base_API_Url.getLink()+"login_social");


    private String link;
    APILinks(String link){
        this.link=link;
    }

    public String getLink() {
        return link;
    }

}
