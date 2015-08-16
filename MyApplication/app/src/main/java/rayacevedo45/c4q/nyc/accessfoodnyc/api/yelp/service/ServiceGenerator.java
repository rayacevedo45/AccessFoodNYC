package rayacevedo45.c4q.nyc.accessfoodnyc.api.yelp.service;

import retrofit.RestAdapter;

/**
 * Created by Hoshiko on 8/16/15.
 */
public class ServiceGenerator {

    public static YelpSearchService createYelpSearchService(){
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(YelpSearchService.BASE_URL)
                .build();

        YelpSearchService service = restAdapter.create(YelpSearchService.class);

        return service;
    }
}
