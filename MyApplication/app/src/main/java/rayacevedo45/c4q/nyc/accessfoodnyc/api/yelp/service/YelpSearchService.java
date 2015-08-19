package rayacevedo45.c4q.nyc.accessfoodnyc.api.yelp.service;

import rayacevedo45.c4q.nyc.accessfoodnyc.api.yelp.models.YelpResponse;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Hoshiko on 8/16/15.
 */
public interface YelpSearchService {

    public static final String BASE_URL = "https://api.yelp.com";

    @GET("/v2/search/?term=food%20truck&limit=10")
    void searchFoodCarts (@Query("location") String loc, Callback<YelpResponse> responseResult);

}
