package nyc.c4q.take2.accessfood;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.parse.ParseUser;


public class SplashActivity extends Activity {
    ImageView accessIM, foodIM, NYCim;
    GifView gifView;

    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//        accessIM = (ImageView) findViewById(R.id.accessID);
//        foodIM = (ImageView) findViewById(R.id.foodID);
//        NYCim = (ImageView) findViewById(R.id.NYCID);




        gifView = (GifView) findViewById(R.id.gif_view);

//        accessIM.startAnimation(AnimationUtils.loadAnimation(SplashActivity.this, R.anim.lr));
//        foodIM.startAnimation(AnimationUtils.loadAnimation(SplashActivity.this, R.anim.rl));
//        NYCim.startAnimation(AnimationUtils.loadAnimation(SplashActivity.this, R.anim.lr));


        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();


        if (isUserLoggedIn()) {
            goToMapsActivity();
        } else {
            new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    // Start your app main activity
                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i);

                    // close this activity
                    finish();
                }
            }, 2000);
        }


    }

    private boolean isUserLoggedIn() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            return true;
        } else {
            AccessToken currentToken = AccessToken.getCurrentAccessToken();
            if (currentToken != null && !currentToken.isExpired()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void goToMapsActivity() {
        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
