package me.gostalk.stalkme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends Activity {

    // Email, password edittext
    EditText txtUsername, txtPassword;

    // login button
    Button btnLogin;

    // Alert Dialog Manager
    AlertDialogManager alert = new AlertDialogManager();

    // Session Manager Class
    SessionManager session;

    RequestQueue requestQueue;

    Boolean Confirmed = false;

    private final static String Login_URL = "http://api.gostalk.me/login/"; // All API calls go here

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestQueue = Volley.newRequestQueue(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        // Session Manager
        session = new SessionManager(getApplicationContext());

        // Email, Password input text
        txtUsername = (EditText) findViewById(R.id.txtUsername);
        txtPassword = (EditText) findViewById(R.id.txtPassword);

        Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();


        // Login button
        btnLogin = (Button) findViewById(R.id.btnLogin);


        // Login button click event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Get username, password from EditText
                String username = txtUsername.getText().toString();
                String password = txtPassword.getText().toString();
                try {
                    password = AeSimpleSHA1.SHA1(password);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                // Check if username, password is filled
                if (username.trim().length() > 0 && password.trim().length() > 0) {
                    // For testing puspose username, password is checked with sample data
                    // username = test
                    // password = test
                    postLogin(username, password);
                } else {
                    // user didn't entered username or password
                    // Show alert asking him to enter the details
                    alert.showAlertDialog(LoginActivity.this, "Login failed..", "Please enter username and password", false);
                }

            }
        });
    }

    private void login(String stringResponse, String username, String passhash) {
        try {
            JSONObject response = new JSONObject(stringResponse);
            String code = response.getJSONObject("meta").getString("code");
            if (code.equals("200")) {
                Confirmed = true;
            }
        } catch (Exception ex) {
            Confirmed = false;
            ex.printStackTrace();
        }

        if (Confirmed) {
            // Creating user login session
            // For testing i am storing name, email as follow
            // Use user real data
            session.createLoginSession(username, passhash);

            // Staring MainActivity
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            finish();
        } else {
            // username / password doesn't match
            alert.showAlertDialog(LoginActivity.this, "Login failed..", "Username/Password is incorrect", false);
        }
    }

    /**
     * @param username, user's username
     * @param passhash, user's password hash
     */
    private void postLogin(final String username, final String passhash) {
        String URL = Login_URL;
        try {
            URL += "?" + "username=" + URLEncoder.encode(username, "UTF-8");
            URL += "&" + "passhash=" + URLEncoder.encode(passhash, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.wtf("Login", e);
        }

        StringRequest jsLoginPostRequest = new StringRequest( Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        login(response, username, passhash);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LoginQuery", "Failed to login " + error);
            }
        });
        requestQueue.add(jsLoginPostRequest);
    }
}