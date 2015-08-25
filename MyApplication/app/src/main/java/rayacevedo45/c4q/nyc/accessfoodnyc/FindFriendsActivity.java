package rayacevedo45.c4q.nyc.accessfoodnyc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;

public class FindFriendsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friends);

        AccessToken currentToken = AccessToken.getCurrentAccessToken();



    }
}
