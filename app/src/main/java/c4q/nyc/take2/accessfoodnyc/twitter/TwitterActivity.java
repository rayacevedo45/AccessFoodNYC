//package rayacevedo45.c4q.nyc.accessfoodnyc.twitter;
//
//import android.app.Activity;
//import android.app.ProgressDialog;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.widget.ListView;
//import android.widget.Toast;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import rayacevedo45.c4q.nyc.accessfoodnyc.R;
//import twitter4j.Twitter;
//import twitter4j.TwitterException;
//import twitter4j.TwitterFactory;
//import twitter4j.auth.OAuth2Token;
//import twitter4j.conf.ConfigurationBuilder;
//
///**
// * Created by Hoshiko on 8/28/15.
// */
//public class TwitterActivity extends Activity {
//
//    private final String TWIT_CONS_KEY = "gJdL8HCQ5aOOo5Px229UUZW0z";
//    private final String TWIT_CONS_SEC_KEY = "ELSs2Bwujyud3D0MT1q8DLNTp2eGCrP1YfKbP6P1aVtZY3zmmU";
//    ListView list;
//    private String twitterHandle = "LukesLobster";
//    Twitter twitter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_twitter);
//        list = (ListView) findViewById(R.id.list);
//
//        new SearchOnTwitter().execute();
//    }
//
//
//
//
//    class SearchOnTwitter extends AsyncTask<String, Void, ArrayList<String>> {
//        ArrayList<Tweet> tweets;
//        final int SUCCESS = 0;
//        final int FAILURE = SUCCESS + 1;
//        ProgressDialog dialog;
//        ArrayList<String> tweetList;
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            dialog = ProgressDialog.show(TwitterActivity.this, "", "searching tweets");
//        }
//
//        @Override
//        protected ArrayList<String> doInBackground(String... params) {
//            try {
//                ConfigurationBuilder builder = new ConfigurationBuilder();
//                builder.setApplicationOnlyAuthEnabled(true);
//                builder.setOAuthConsumerKey(TWIT_CONS_KEY);
//                builder.setOAuthConsumerSecret(TWIT_CONS_SEC_KEY);
//
//                OAuth2Token token = new TwitterFactory(builder.build()).getInstance().getOAuth2Token();
//
//                builder = new ConfigurationBuilder();
//                builder.setApplicationOnlyAuthEnabled(true);
//                builder.setOAuthConsumerKey(TWIT_CONS_KEY);
//                builder.setOAuthConsumerSecret(TWIT_CONS_SEC_KEY);
//                builder.setOAuth2TokenType(token.getTokenType());
//                builder.setOAuth2AccessToken(token.getAccessToken());
//
//                twitter = new TwitterFactory(builder.build()).getInstance();
//
//                List<twitter4j.Status> statuses2 = null;
//                try {
//                    statuses2 = twitter.getUserTimeline(twitterHandle);
//                } catch (TwitterException e1) {
//                    e1.printStackTrace();
//                }
//
//                tweetList = new ArrayList<String>();
//                for(int i = 0; i < statuses2.size(); i++)
//                {
//                    twitter4j.Status status = statuses2.get(i);
//                    String tweettext =(status.getUser().getName()) + ":" + (status.getText())+ "\n (" + status.getCreatedAt().toLocaleString() + ")";
//
//
//                    tweetList.add(tweettext);
//                }
//
//                return tweetList;
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return null;
//
//        }
//
//        @Override
//        protected void onPostExecute(ArrayList<String> tweetList) {
//
//            dialog.dismiss();
//            if (tweetList !=null) {
//                list.setAdapter(new TweetAdapter(TwitterActivity.this, tweetList));
//            } else {
//                Toast.makeText(TwitterActivity.this, "Error while searching tweets", Toast.LENGTH_LONG).show();
//            }
//        }
//    }
//
//
//}
