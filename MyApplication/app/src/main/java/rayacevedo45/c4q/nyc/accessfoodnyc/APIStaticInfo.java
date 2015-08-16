package rayacevedo45.c4q.nyc.accessfoodnyc;

/**
 * Created by Hoshiko on 8/15/15.
 */
public class APIStaticInfo {

    private final String YELP_CONSUMER_KEY ="BrL42zhoUCPLZCO1D5b7LQ";
    private final String YELP_CONSUMER_SECRET = "ZsBqxMKEI4QEipoFdlmwadnyb8Y";
    private final String YELP_TOKEN = "ilXQIAE-HffHzEdxnasVZ1uNrePI8wM";
    private final String YELP_TOKEN_SECRET = "7I1jh-uEEJuq1akXSm5dkVn6U_w";


    public String getYelpConsumerKey(){
        return YELP_CONSUMER_KEY;
    }

    public String getYelpConsumerSecret(){
        return YELP_CONSUMER_SECRET;
    }

    public String getYelpToken(){
        return YELP_TOKEN;
    }

    public String getYelpTokenSecret(){
        return YELP_TOKEN_SECRET;
    }
}
