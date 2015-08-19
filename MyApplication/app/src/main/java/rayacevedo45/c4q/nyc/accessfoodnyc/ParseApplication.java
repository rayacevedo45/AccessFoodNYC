package rayacevedo45.c4q.nyc.accessfoodnyc;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;

import rayacevedo45.c4q.nyc.accessfoodnyc.api.yelp.models.YelpResponse;

public class ParseApplication extends Application {

    private static ParseApplication instance;
    public static YelpResponse yelpResponse;

    public static ParseApplication getInstance() {
        return instance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
		/*
		 * Add Parse initialization code here
		 */

        Parse.initialize(this);

        ParseACL defaultACL = new ParseACL();

        // If you would like all objects to be private by default, remove this
        // line.
        defaultACL.setPublicReadAccess(true);

        ParseACL.setDefaultACL(defaultACL, true);
    }
}

