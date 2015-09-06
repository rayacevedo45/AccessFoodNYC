package rayacevedo45.c4q.nyc.accessfoodnyc;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import rayacevedo45.c4q.nyc.accessfoodnyc.twitter.Tweet;
import rayacevedo45.c4q.nyc.accessfoodnyc.twitter.TweetAdapter;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.OAuth2Token;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterFragment extends Fragment {

//    ParseObject vendor = new ParseObject("Vendor");
    ImageView menuImage;
//    String menuPicUrlStr;

    private final String TWIT_CONS_KEY = "TovzZmg4TOmqS0rAuN1bH8gSU";
    private final String TWIT_CONS_SEC_KEY = "pwlybwQE58GMApEZsttz0T3bXOXR7Dvttp9MNugegUGryiDK4s";
    ListView list;
    Twitter twitter;
    private String twitterHandle;
//    public String twitterHandle = "smorgasburg";

    private ParseObject selectedVendor;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_twitter, container, false);

        list = (ListView) rootView.findViewById(R.id.list);

        getTwitterHandle();

//        twitterHandle  = getArguments().getString("Twitter Handle");

//        new SearchOnTwitter().execute();

        return rootView;


    }



    @Override
    public void onResume() {
        super.onResume();



    }


    class SearchOnTwitter extends AsyncTask<String, Void, ArrayList<String>> {
        ArrayList<Tweet> tweets;
        final int SUCCESS = 0;
        final int FAILURE = SUCCESS + 1;
        ProgressDialog dialog;
        ArrayList<String> tweetList;



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(getActivity(), "", "searching tweets");
        }

        @Override
        protected ArrayList<String> doInBackground(String... params) {
            try {
                ConfigurationBuilder builder = new ConfigurationBuilder();
                builder.setApplicationOnlyAuthEnabled(true);
                builder.setOAuthConsumerKey(TWIT_CONS_KEY);
                builder.setOAuthConsumerSecret(TWIT_CONS_SEC_KEY);

                OAuth2Token token = new TwitterFactory(builder.build()).getInstance().getOAuth2Token();

                builder = new ConfigurationBuilder();
                builder.setApplicationOnlyAuthEnabled(true);
                builder.setOAuthConsumerKey(TWIT_CONS_KEY);
                builder.setOAuthConsumerSecret(TWIT_CONS_SEC_KEY);
                builder.setOAuth2TokenType(token.getTokenType());
                builder.setOAuth2AccessToken(token.getAccessToken());

                twitter = new TwitterFactory(builder.build()).getInstance();

                List<twitter4j.Status> statuses2 = null;
                try {
                    statuses2 = twitter.getUserTimeline(twitterHandle);
                } catch (TwitterException e1) {
                    e1.printStackTrace();
                }

                tweetList = new ArrayList<String>();
                for(int i = 0; i < statuses2.size(); i++)
                {
                    twitter4j.Status status = statuses2.get(i);
                    String tweettext =(status.getUser().getName()) + ":" + (status.getText())+ "\n (" + status.getCreatedAt().toLocaleString() + ")";


                    tweetList.add(tweettext);
                }

                return tweetList;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(ArrayList<String> tweetList) {

            dialog.dismiss();
            if (tweetList !=null) {
                list.setAdapter(new TweetAdapter(getActivity(), tweetList));
            } else {
                Toast.makeText(getActivity(), "Error while searching tweets", Toast.LENGTH_LONG).show();
            }
        }
    }


    protected void getTwitterHandle(){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Vendor");
        query.whereEqualTo("yelpId", MapsActivity.businessId);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    String twitterName = object.getString("twitter");
                    String id = object.getObjectId();
                    twitterHandle = twitterName;
                    new SearchOnTwitter().execute();
                } else {
                    Log.d("vendor", "twitterhandle request failed.");
                }
            }
        });



    }

}
