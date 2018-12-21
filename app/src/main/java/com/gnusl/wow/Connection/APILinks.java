package com.gnusl.wow.Connection;

public enum APILinks {

    Base_API_Url("http://wow.gnusl.com/api/"),
    Base_Media_Url("http://wow.gnusl.com/public/uploads/"),

    // User
    Register_Url(Base_API_Url.getLink()+"register"),
    Login_Url(Base_API_Url.getLink()+"login"),
    Login_Social_Url(Base_API_Url.getLink()+"login_social"),

    // Channels
    Channels_Url(Base_API_Url.getLink()+"channel"),
    Get_Channels_Type_Url(Channels_Url.getLink()+"/types"),

    // Posts
    Featured_Posts_Url(Base_API_Url.getLink()+"post"),
    Upload_Image_Url(Base_API_Url.getLink()+"image/upload"),

    // like
    Update_Like_Url(Base_API_Url.getLink()+"like"),

    // Comments
    Comments_Url(Base_API_Url.getLink()+"comment"),

    // Following
    Follow_Url(Base_API_Url.getLink()+"follow"),
    Posts_By_Following(Follow_Url.getLink()+"/post");

    private String link;
    APILinks(String link){
        this.link=link;
    }

    public String getLink() {
        return link;
    }

}
