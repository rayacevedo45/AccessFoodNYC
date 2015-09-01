package rayacevedo45.c4q.nyc.accessfoodnyc.twitter;

/**
 * Created by Hoshiko on 8/28/15.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import rayacevedo45.c4q.nyc.accessfoodnyc.R;

public class TweetAdapter extends BaseAdapter {
    ArrayList<String> tweetList;
    Context context;

    public TweetAdapter(Context context, ArrayList<String> tweetList) {
        this.tweetList = tweetList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return tweetList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.tweet_list_item, null);
        }

        String tweet = tweetList.get(position);
        TextView txtTweet = (TextView) convertView.findViewById(R.id.txtTweet);
//        TextView txtTweetBy = (TextView) convertView.findViewById(R.id.txtTweetBy);

        txtTweet.setText(tweet);
//        txtTweetBy.setText(tweet);

        return convertView;
    }
}