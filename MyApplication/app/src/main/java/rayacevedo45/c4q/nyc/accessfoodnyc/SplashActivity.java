package rayacevedo45.c4q.nyc.accessfoodnyc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    private TextView mTextViewAccess;
    private TextView mTextViewFood;
    private TextView mTextViewNYC;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mTextViewAccess = (TextView) findViewById(R.id.accessID);
        mTextViewFood = (TextView) findViewById(R.id.foodID);
        mTextViewNYC = (TextView) findViewById(R.id.NYCID);

        mTextViewAccess.startAnimation(AnimationUtils.loadAnimation(SplashActivity.this, R.anim.lr));
        mTextViewFood.startAnimation(AnimationUtils.loadAnimation(SplashActivity.this, R.anim.rl));
        mTextViewNYC.startAnimation(AnimationUtils.loadAnimation(SplashActivity.this, R.anim.lr));

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {


                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashActivity.this, MapsActivity.class);
                startActivity(i);

                // close this activity
                finish();
            }
        }, 2900);
    }


}
