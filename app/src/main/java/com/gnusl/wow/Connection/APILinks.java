package com.gnusl.wow.Connection;

public enum APILinks {

    //Base_API_Url("http://gnusl.com/demo/wow/api/"),
    Base_API_Url("http://fat7al.com/wow/api/"),
    Base_Media_Url("http://fat7al.com/wow/public/uploads/"),
    Base_Socket_Streaming_Url("http://gnusl.com:3000/streams/"),

    // User
    Base_User_Url(Base_API_Url.getLink() + "user"),
    Register_Url(Base_API_Url.getLink() + "register"),
    Login_Url(Base_API_Url.getLink() + "login"),
    Login_Social_Url(Base_API_Url.getLink() + "login_social"),
    Search_User_Url(Base_User_Url.getLink() + "/search"),

    // Channels
    Channels_Url(Base_API_Url.getLink() + "channel"),
    Channels_Lock_Type_Url(Channels_Url.getLink() + "/lock_types"),
    Get_Channels_Type_Url(Channels_Url.getLink() + "/types"),
    Explore_Channels_Url(Channels_Url.getLink() + "/explore"),
    Explore_Gifts_Url(Explore_Channels_Url.getLink() + "/gift"),
    Follow_Channel_Url(Channels_Url.getLink() + "/follow"),
    Get_Pin_Top_Url(Channels_Url.getLink() + "/priority"),

    // Gifts
    Gifts_Url(Base_API_Url.getLink() + "gift"),

    // Posts
    Featured_Posts_Url(Base_API_Url.getLink() + "post"),
    Upload_Image_Url(Base_API_Url.getLink() + "image/upload"),

    // like
    Update_Like_Url(Base_API_Url.getLink() + "like"),

    // Comments
    Comments_Url(Base_API_Url.getLink() + "comment"),

    // Message
    Message_Url(Base_API_Url.getLink() + "message"),
    System_Message_Url(Base_API_Url.getLink() + "system/message"),

    // Earn Gold
    Earn_Gold_Url(Base_API_Url.getLink() + "gold"),

    // Ads
    Advertisement_Url(Base_API_Url.getLink() + "ad"),

    // badges
    All_Badges_Url(Base_API_Url.getLink() + "badge"),
    My_Profile_Badges_Url(Base_User_Url.getLink() + "/user/badges?take=10000000&skip=0"),

    // Following
    Follow_Url(Base_API_Url.getLink() + "follow"),
    Posts_By_Following(Follow_Url.getLink() + "/post"),

    // aristocracy
    Get_Aristocracy_Url(Base_API_Url.getLink() + "aristocracy"),

    // payment
    Store_Payment(Base_API_Url.getLink() + "payment"),
    Get_Payment_Packages(Store_Payment.getLink() + "/packages"),

    // specials
    Get_Special_ids(Base_API_Url.getLink() + "special/ids"),

    // daily login
    Daily_Login_url(Base_API_Url.getLink() + "daily");

    private String link;

    APILinks(String link) {
        this.link = link;
    }

    public String getLink() {
        return link;
    }

}
