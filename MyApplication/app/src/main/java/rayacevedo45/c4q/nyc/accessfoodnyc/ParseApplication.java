package rayacevedo45.c4q.nyc.accessfoodnyc;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseFacebookUtils;
import com.parse.ParseSession;

import rayacevedo45.c4q.nyc.accessfoodnyc.api.yelp.models.YelpResponse;

public class ParseApplication extends Application {

    private static ParseApplication sInstance;
    public static YelpResponse sYelpResponse;

    public static ParseApplication getInstance() {
        return sInstance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
		/*
		 * Add Parse initialization code here
		 */

        Parse.initialize(this);
        ParseFacebookUtils.initialize(this);


        ParseACL defaultACL = new ParseACL();

        // If you would like all objects to be private by default, remove this
        // line.
        defaultACL.setPublicReadAccess(true);

        ParseACL.setDefaultACL(defaultACL, true);
    }
}

