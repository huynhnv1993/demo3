package com.example.windows10gamer.demo3.ultil;

/**
 * Created by Windows 10 Gamer on 26/06/2017.
 */

public class Server {

    public static String localhost = "210.245.89.15/api/v1";
    public static String Urlsignin = "http://" + localhost + "/users/login?"; //2pay.vn/api/v1/users/login?username=0936382222&password=1qa1qa1qa
    public static String Urlcardphone = "http://" + localhost + "/cards/types?type=phone"; //2pay.vn/api/v1/cards/types?type=phone
    public static String Urlprofile = "http://" + localhost + "/users/profile?"; //2pay.vn/api/v1/users/profile?
    public static String Urlbuycard = "http://" + localhost + "/cards/buynew?data="; //2pay.vn/api/v1/cards/buynew?data=[{"type":"PREPAIDCARD","cardTypeId":30,"qty":1}]
}
