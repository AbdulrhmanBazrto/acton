package com.gnusl.wow.Connection;

public enum APILinks {

    //Base_API_Url("http://gnusl.com/demo/wow/api/"),
    Base_API_Url("http://fat7al.com/wow/api/"),
    Base_Media_Url("http://fat7al.com/wow/public/uploads/"),
    Base_Socket_Streaming_Url("http://gnusl.com:3000/streams/"),

    // User
    Base_User_Url(Base_API_Url.getLink()+"user"),
    Register_Url(Base_API_Url.getLink()+"register"),
    Login_Url(Base_API_Url.getLink()+"login"),
    Login_Social_Url(Base_API_Url.getLink()+"login_social"),
    Search_User_Url(Base_User_Url.getLink()+"/search"),

    // Channels
    Channels_Url(Base_API_Url.getLink()+"channel"),
    Channels_Lock_Type_Url(Channels_Url.getLink()+"/lock_types"),
    Get_Channels_Type_Url(Channels_Url.getLink()+"/types"),
    Explore_Channels_Url(Channels_Url.getLink()+"/explore"),
    Explore_Gifts_Url(Explore_Channels_Url.getLink()+"/gift"),

    // Gifts
    Gifts_Url(Base_API_Url.getLink()+"gift"),

    // Posts
    Featured_Posts_Url(Base_API_Url.getLink()+"post"),
    Upload_Image_Url(Base_API_Url.getLink()+"image/upload"),

    // like
    Update_Like_Url(Base_API_Url.getLink()+"like"),

    // Comments
    Comments_Url(Base_API_Url.getLink()+"comment"),

    // Message
    Message_Url(Base_API_Url.getLink()+"message"),
    System_Message_Url(Base_API_Url.getLink()+"system/message"),

    // Earn Gold
    Earn_Gold_Url(Base_API_Url.getLink()+"gold"),

    // badges
    Profile_Badges_Url(Base_User_Url.getLink()+"/badges"),

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
