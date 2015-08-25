package rayacevedo45.c4q.nyc.accessfoodnyc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FindFriendsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friends);

        AccessToken currentToken = AccessToken.getCurrentAccessToken();



        GraphRequest request = GraphRequest.newMyFriendsRequest(currentToken, new GraphRequest.GraphJSONArrayCallback() {
            @Override
            public void onCompleted(JSONArray jsonArray, GraphResponse graphResponse) {
                Log.i("SHOW ME!!!!!", jsonArray.toString());
                Log.i("SHOW ME!!!!!!!!!", graphResponse.toString());

            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link");
        request.setParameters(parameters);
        request.executeAsync();

        GraphRequest.executeBatchAsync(request);
    }
}
