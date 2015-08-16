//package rayacevedo45.c4q.nyc.accessfoodnyc;
//
//import android.os.Bundle;
//
//import org.json.JSONException;
//
//import java.util.ArrayList;
//
///**
// * Created by Hoshiko on 8/16/15.
// */
//public class ApiTest {
//
//    protected void my_Class()
//    {
//        API_Static_Stuff api_keys = new API_Static_Stuff();
//
//        Yelp yelp = new Yelp(api_keys.getYelpConsumerKey(), api_keys.getYelpConsumerSecret(),
//                api_keys.getYelpToken(), api_keys.getYelpTokenSecret());
//        String response = yelp.search("burritos", 30.361471, -87.164326);
//
//        YelpParser yParser = new YelpParser();
//        yParser.setResponse(response);
//        try {
//            yParser.parseBusiness();
//        } catch (JSONException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//            //Do whatever you want with the error, like throw a Toast error report
//        }
//
//        int i = 0;
//        String mobile_url = yParser.getBusinessMobileURL(i);
//        String rating_url = yParser.getRatingURL(i);
//        String b_name = yParser.getBusinessName(i);
//        Bundle tmpBundle = yParser.getYelpBundle();
//        ArrayList<String> tmpKeys = yParser.getBundleKeys();
//    }
//
//}
