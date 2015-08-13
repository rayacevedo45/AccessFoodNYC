package rayacevedo45.c4q.nyc.accessfoodnyc.accounts;

import android.accounts.Account;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.IOException;

import rayacevedo45.c4q.nyc.accessfoodnyc.MapsActivity;
import rayacevedo45.c4q.nyc.accessfoodnyc.R;
import rayacevedo45.c4q.nyc.accessfoodnyc.ParseApplication;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private static final String TAG = "LoginActivity";

    private static final int RC_SIGN_IN = 0;

    // Is there a ConnectionResult resolution in progress?
    private boolean mIsResolving = false;

    // Should we automatically resolve ConnectionResults when possible?
    private boolean mShouldResolve = false;

    private GoogleApiClient mGoogleApiClient;

    protected EditText usernameEditText;
    protected EditText passwordEditText;
    protected Button loginButton;
    private SignInButton mSignInButton;
    private Button mButtonTwitter;

    private Button mButtonGoogleSignOut;
    private Button mButtonTwitterSignOut;

    protected TextView signUpTextView;

    protected ParseApplication app;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ParseTwitterUtils.initialize("5FE9QgpI9yl1u8rsrVJKl09i6", "wvmcQrGJggj1FlVDo58PUODdSF9DWgu9KID6myCUmHBq9UfYxI");

        // Build GoogleApiClient with access to basic profile
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(new Scope(Scopes.PROFILE))
                .build();
//        Parse.initialize(this);
//
//        ParseACL defaultACL = new ParseACL();
//
//        // If you would like all objects to be private by default, remove this
//        // line.
//        defaultACL.setPublicReadAccess(true);
//
//        ParseACL.setDefaultACL(defaultACL, true);
//
//        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        app = new ParseApplication();

        initializeViews();


        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage(R.string.login_error_message)
                            .setTitle(R.string.login_error_title)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else {
                    setProgressBarIndeterminateVisibility(true);

                    ParseUser.logInInBackground(username, password, new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {
                            setProgressBarIndeterminateVisibility(false);

                            if (e == null) {
                                // Success!
                                Intent intent = new Intent(LoginActivity.this, MapsActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            } else {
                                // Fail
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setMessage(e.getMessage())
                                        .setTitle(R.string.login_error_title)
                                        .setPositiveButton(android.R.string.ok, null);
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        }
                    });
                }
            }
        });
    }

    private void initializeViews() {
        signUpTextView = (TextView)findViewById(R.id.signUpText);
        usernameEditText = (EditText)findViewById(R.id.usernameField);
        passwordEditText = (EditText)findViewById(R.id.passwordField);
        loginButton = (Button)findViewById(R.id.loginButton);
        mSignInButton = (SignInButton) findViewById(R.id.sign_in_button);
        mButtonTwitter = (Button) findViewById(R.id.button_twitter);
        mButtonGoogleSignOut = (Button) findViewById(R.id.button_google_sign_out);
        mButtonTwitterSignOut = (Button) findViewById(R.id.button_twitter_sign_out);
    }

    private void setUpListeners(boolean isResumed) {
        if (isResumed) {
            mSignInButton.setOnClickListener(this);
            mButtonTwitter.setOnClickListener(this);
            mButtonGoogleSignOut.setOnClickListener(this);
            mButtonTwitterSignOut.setOnClickListener(this);
        } else {
            mSignInButton.setOnClickListener(null);
            mButtonTwitter.setOnClickListener(null);
            mButtonGoogleSignOut.setOnClickListener(null);
            mButtonTwitterSignOut.setOnClickListener(null);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(Bundle bundle) {
        // onConnected indicates that an account was selected on the device, that the selected
        // account has granted any requested permissions to our app and that we were able to
        // establish a service connection to Google Play services.
        Log.d(TAG, "onConnected:" + bundle);
        mShouldResolve = false;

        // Show the signed-in UI
        //showSignedInUI();
        Toast.makeText(getApplicationContext(), "Logged in via Google", Toast.LENGTH_SHORT).show();
        //goToMapsActivity();
        new GetIdTokenTask().execute();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (!mIsResolving && mShouldResolve) {
            if (connectionResult.hasResolution()) {
                try {
                    connectionResult.startResolutionForResult(this, RC_SIGN_IN);
                    mIsResolving = true;
                } catch (IntentSender.SendIntentException e) {
                    Log.e(TAG, "Could not resolve ConnectionResult.", e);
                    mIsResolving = false;
                    mGoogleApiClient.connect();
                }
            } else {
                // Could not resolve the connection result, show the user an
                // error dialog.
                //showErrorDialog(connectionResult);
            }
        } else {
            // Show the signed-out UI
            //showSignedOutUI();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpListeners(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        setUpListeners(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                onSignInClicked();
                break;
            case R.id.button_twitter:
                signInViaTwitter();
                break;
            case R.id.button_google_sign_out:
                signOutFromGoogle();
                break;
            case R.id.button_twitter_sign_out:
                break;
        }
    }

    private void signOutFromGoogle() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            Toast.makeText(getApplicationContext(), "Signed out from Google", Toast.LENGTH_SHORT).show();
        }
    }

    private void signInViaTwitter() {
        ParseTwitterUtils.logIn(this, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {
                if (user == null) {
                    Log.d("MyApp", "Uh oh. The user cancelled the Twitter login.");
                    Toast.makeText(getApplicationContext(), "Something is wrong", Toast.LENGTH_SHORT).show();
                } else if (user.isNew()) {
                    Log.d("MyApp", "User signed up and logged in through Twitter!");
                    Toast.makeText(getApplicationContext(), "Logged in via Twitter", Toast.LENGTH_SHORT).show();
                    goToMapsActivity();
                } else if (!ParseTwitterUtils.isLinked(user)) {

                    ParseTwitterUtils.link(user, getApplicationContext(), new SaveCallback() {
                        @Override
                        public void done(ParseException ex) {

                        }
                    });
                } else {
                    Log.d("MyApp", "User logged in through Twitter!");
                }
            }
        });


    }

    private void onSignInClicked() {
        // User clicked the sign-in button, so begin the sign-in process and automatically
        // attempt to resolve any errors that occur.
        mShouldResolve = true;
        mGoogleApiClient.connect();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult:" + requestCode + ":" + resultCode + ":" + data);

        if (requestCode == RC_SIGN_IN) {
            // If the error resolution was not successful we should not resolve further.
            if (resultCode != RESULT_OK) {
                mShouldResolve = false;
            }

            mIsResolving = false;
            mGoogleApiClient.connect();
        }
    }

    private void goToMapsActivity() {
        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
        startActivity(intent);
        finish();
    }

    private class GetIdTokenTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            String accountName = Plus.AccountApi.getAccountName(mGoogleApiClient);
            Account account = new Account(accountName, GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
            //String scopes = "audience:server:client_id:" + getString(R.string.parse_client_key);// Not the app's client ID.
            String scopes = "audience:server:client_id:" + "853411645-rv674lrkr139etcu7ksqh167cuv55i9f.apps.googleusercontent.com";
            try {
                return GoogleAuthUtil.getToken(getApplicationContext(), account, scopes);
            } catch (IOException e) {
                Log.e(TAG, "Error retrieving ID token.", e);
                return null;
            } catch (GoogleAuthException e) {
                Log.e(TAG, "Error retrieving ID token.", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Log.i(TAG, "ID token: " + result);
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            if (result != null) {
                // Successfully retrieved ID Token
                // ...

            } else {
                // There was some error getting the ID Token
                // ...
            }
        }

    }
}
