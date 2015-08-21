package rayacevedo45.c4q.nyc.accessfoodnyc;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.parse.ParseUser;


public class ReviewsFragment extends Fragment implements View.OnClickListener {

    private Button mButtonReview;
    private ListView mListView;
    private String objectId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_reviews, container, false);

        mButtonReview = (Button) rootView.findViewById(R.id.button_review);
        mListView = (ListView) rootView.findViewById(R.id.listView_reviews);
        objectId = getArguments().getString(Constants.EXTRA_KEY_VENDOR_OBJECT_ID);

        ParseUser user = ParseUser.getCurrentUser();


        return rootView;
    }

    private void setUpListener(boolean isResumed) {
        if (isResumed) {
            mButtonReview.setOnClickListener(this);
        } else {
            mButtonReview.setOnClickListener(null);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpListener(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        setUpListener(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_review:
                writeReview();
                break;
        }
    }

    private void writeReview() {

        ParseUser user = ParseUser.getCurrentUser();
        String userId = user.getObjectId();



    }

    private class ReviewAdapter extends BaseAdapter {

        private Context mContext;


        public ReviewAdapter(Context context) {
            mContext = context;
        }

        @Override
        public int getCount() {
            return 0;
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
            return null;
        }
    }
}
