package rayacevedo45.c4q.nyc.accessfoodnyc.database;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;

/**
 * Created by Hoshiko on 8/12/15.
 */
public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

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

