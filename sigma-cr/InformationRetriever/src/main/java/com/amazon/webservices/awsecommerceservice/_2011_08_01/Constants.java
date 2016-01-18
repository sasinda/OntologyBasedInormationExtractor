package com.amazon.webservices.awsecommerceservice._2011_08_01;

/**
 * Created with IntelliJ IDEA.
 * User: hasinthaindrajee
 * Date: 6/6/13
 * Time: 3:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class Constants {

    public static String defaultSaveLocation="/home/hasinthaindrajee/Desktop/reviews";
    public static String SpiderJsLocation="";
    private static String amazonAcessId="AKIAJ5XINUVT67WU4KAA";
    private static String amazonSecurityKey="lR60PrFwoGuTHaJzdzyQOpfWbDRhvAGou5jRBESs";
    public static int no_of_pages=10;

    public static String getAmazonSecurityKey(String pw) {
        if("sigmacr".equals(pw)){
            return amazonSecurityKey;
        }
        else{
            System.out.println("not authenticated to get amazon credentials");
            return null;
        }
    }

    public static String getAmazonAcessId(String pw)
    {  if("sigmacr".equals(pw)){
        return amazonAcessId;
    }
    else{
        System.out.println("not authenticated to get amazon credentials");
        return null;
    }


    }




}
