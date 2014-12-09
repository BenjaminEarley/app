package me.gostalk.stalkme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Ryan on 12/8/2014.
 */
public class RegisterActivity extends Activity
{
    // Email, password edittext
    EditText txtUsername, txtPassword, txtPassword2;

    // login button
    Button btnLogin;

    // Alert Dialog Manager
    AlertDialogManager alert = new AlertDialogManager();

    // Session Manager Class
    SessionManager session;

    RequestQueue requestQueue;

    Boolean Confirmed = false;

    private final static String Register_URL = "https://api.gostalk.me/register/"; // All API calls go here

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestQueue = Volley.newRequestQueue(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);


        // Session Manager
        session = new SessionManager(getApplicationContext());

        // Email, Password input text
        txtUsername = (EditText) findViewById(R.id.txtRegisterUsername);
        txtPassword = (EditText) findViewById(R.id.txtRegisterPassword);
        txtPassword2 = (EditText) findViewById(R.id.txtRegisterPassword2);

       // Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();

        // Login button
        btnLogin = (Button) findViewById(R.id.btnRegister);


        // Login button click event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Get username, password from EditText
                String username = txtUsername.getText().toString();
                String password = txtPassword.getText().toString();
                String password2 = txtPassword2.getText().toString();
                try {
                    password = AeSimpleSHA1.SHA1(password);
                    password2 = AeSimpleSHA1.SHA1(password2);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                // Check if username, password is filled, password2 = password
                if (username.trim().length() > 0 && password.trim().length() > 0 && password2.trim().equals(password)) {
                    // For testing puspose username, password is checked with sample data
                    // username = test
                    // password = test
                    postRegister(username, password);
                } else {
                    // user didn't entered username or password
                    // Show alert asking him to enter the details
                    alert.showAlertDialog(RegisterActivity.this, "Register failed..", "Please enter username and password", false);
                }

            }
        });
    }

    private void Register(String stringResponse, String username, String passhash) {
        try {
            JSONObject response = new JSONObject(stringResponse);
            String code = response.getJSONObject("meta").getString("code");
           // String code2 = response.getJSONObject("meta").getString("")304
            if (code.equals("200")) {
                Confirmed = true;
            }
            else if(code.equals("304"))
            {
                alert.showAlertDialog(RegisterActivity.this, "Register failed..", "Username already exists", false);
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
        }
    }

    /**
     * @param username, user's username
     * @param passhash, user's password hash
     */
    private void postRegister(final String username, final String passhash) {
        String URL = Register_URL;
        try {
            URL +=  URLEncoder.encode(username, "UTF-8");
            URL += "?" + "passhash=" + URLEncoder.encode(passhash, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.wtf("Login", e);
        }

        StringRequest jsLoginPostRequest = new StringRequest( Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Register(response, username, passhash);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("RegisterQuery", "Failed to register " + error);
            }
        });
        requestQueue.add(jsLoginPostRequest);
    }
}
