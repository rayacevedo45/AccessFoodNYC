package nyc.c4q.take2.accessfood.yelp;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

public interface YelpSearchService {

    public static final String BASE_URL = "https://api.yelp.com";

    @GET("/v2/search/?term=food%20truck&limit=20")
    void searchFoodCarts (@Query("location") String loc, Callback<YelpResponse> responseResult);

}
