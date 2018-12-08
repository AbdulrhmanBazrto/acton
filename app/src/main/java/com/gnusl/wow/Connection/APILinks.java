package com.gnusl.wow.Connection;

public enum APILinks {

    Base_API_Url("http://wow.gnusl.com/api/"),

    // User
    Register_Url(Base_API_Url.getLink()+"register"),
    Login_Url(Base_API_Url.getLink()+"login"),
    Login_Social_Url(Base_API_Url.getLink()+"login_social"),

    // Channels
    Get_Channels_Url(Base_API_Url.getLink()+"channel"),

    // Posts
    Get_Featured_Posts_Url(Base_API_Url.getLink()+"post"),

    Update_Like_Url(Base_API_Url.getLink()+"like"),

    Get_Comments_Url(Base_API_Url.getLink()+"comment");

    private String link;
    APILinks(String link){
        this.link=link;
    }

    public String getLink() {
        return link;
    }

}
