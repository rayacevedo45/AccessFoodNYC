package rayacevedo45.c4q.nyc.accessfoodnyc.accounts;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.gms.common.SignInButton;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import rayacevedo45.c4q.nyc.accessfoodnyc.MapsActivity;
import rayacevedo45.c4q.nyc.accessfoodnyc.ParseApplication;
import rayacevedo45.c4q.nyc.accessfoodnyc.R;

public class LoginActivity extends Activity {

    protected EditText usernameEditText;
    protected EditText passwordEditText;
    protected EditText usernameEditText2;
    protected EditText passwordEditText2;
    protected EditText emailField;
    protected Button loginButton;
    protected Button signUpButton;
    protected SignInButton googleSignin;
    protected Button backButton;
    protected Button continueButton;
    protected LinearLayout layout;
    protected ParseApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        app = new ParseApplication();

        signUpButton = (Button)findViewById(R.id.signupButtonID);
        usernameEditText = (EditText)findViewById(R.id.usernameField);
        passwordEditText = (EditText)findViewById(R.id.passwordField);
        usernameEditText2 = (EditText)findViewById(R.id.usernameField2);
        passwordEditText2 = (EditText)findViewById(R.id.passwordField2);
        loginButton = (Button)findViewById(R.id.loginButton);
        emailField = (EditText) findViewById(R.id.emailFieldID);
        googleSignin = (SignInButton) findViewById(R.id.googleSigninID);
        backButton = (Button) findViewById(R.id.BackButtonID);
        continueButton = (Button) findViewById(R.id.ContinueButtonID);
        layout = (LinearLayout) findViewById(R.id.layoutID);



        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                username = username.trim();
                password = password.trim();

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

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText2.getText().toString();
                String password = passwordEditText2.getText().toString();
                String email = emailField.getText().toString();

                username = username.trim();
                password = password.trim();
                email = email.trim();

                if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage(R.string.signup_error_message)
                            .setTitle(R.string.signup_error_title)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else {
                    setProgressBarIndeterminateVisibility(true);

                    ParseUser newUser = new ParseUser();
                    newUser.setUsername(username);
                    newUser.setPassword(password);
                    newUser.setEmail(email);
                    newUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            setProgressBarIndeterminateVisibility(false);

                            if (e == null) {
                                // Success!
                                Intent intent = new Intent(LoginActivity.this, MapsActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setMessage(e.getMessage())
                                        .setTitle(R.string.signup_error_title)
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
    public void showSignUpFields (View v){

            googleSignin.setVisibility(View.GONE);
            usernameEditText.setVisibility(View.GONE);
            passwordEditText.setVisibility(View.GONE);
            usernameEditText2.setVisibility(View.VISIBLE);
            passwordEditText2.setVisibility(View.VISIBLE);
            emailField.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.GONE);
            signUpButton.setVisibility(View.GONE);
            backButton.setVisibility(View.VISIBLE);
            continueButton.setVisibility(View.VISIBLE);
            layout.setVisibility(View.GONE);

        }
    public void back (View v){
        googleSignin.setVisibility(View.VISIBLE);
        usernameEditText.setVisibility(View.VISIBLE);
        passwordEditText.setVisibility(View.VISIBLE);
        usernameEditText2.setVisibility(View.GONE);
        passwordEditText2.setVisibility(View.GONE);
        emailField.setVisibility(View.GONE);
        loginButton.setVisibility(View.VISIBLE);
        signUpButton.setVisibility(View.VISIBLE);
        backButton.setVisibility(View.GONE);
        continueButton.setVisibility(View.GONE);
        layout.setVisibility(View.VISIBLE);
        }
    }


