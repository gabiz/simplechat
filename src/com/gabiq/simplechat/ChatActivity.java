package com.gabiq.simplechat;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class ChatActivity extends Activity {
    private static final String TAG = ChatActivity.class.getName();
    private static String sUserId;

    public static final String USER_ID_KEY = "userId";

    private EditText etMessage;
    private Button btSend;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        // User login
        if (ParseUser.getCurrentUser() != null) { // start with existing user
            startWithCurrentUser();
        } else { // If not logged in, login as a new anonymous user
            login();
        }
    }
    
    // Get the userId from the cached currentUser object
    private void startWithCurrentUser() {
        sUserId = ParseUser.getCurrentUser().getObjectId();     
        setupMessagePosting();
    }
    
    // Create an anonymous user using ParseAnonymousUtils and set sUserId 
    private void login() {
        ParseAnonymousUtils.logIn(new LogInCallback() {
        @Override
        public void done(ParseUser user, ParseException e) {
                if (e != null) {
                    Log.d(TAG, "Anonymous login failed.");
                } else {
                    startWithCurrentUser();
                }
            }
       });      
    }


    // Setup button event handler which posts the entered message to Parse
    private void setupMessagePosting() {
            // Find the text field and button
            etMessage = (EditText) findViewById(R.id.etMessage);
            btSend = (Button) findViewById(R.id.btSend);
            // When send button is clicked, create message object on Parse
            btSend.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    String data = etMessage.getText().toString();
                    ParseObject message = new ParseObject("Message");
                    message.put(USER_ID_KEY, sUserId);
                    message.put("body", data);
                    message.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            Toast.makeText(ChatActivity.this, "Successfully created message on Parse",
                                 Toast.LENGTH_SHORT).show();
                        }
                    });
                    etMessage.setText("");
                }
            });
    }
}