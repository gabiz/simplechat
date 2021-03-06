package com.gabiq.simplechat;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.gabiq.simplechat.adapters.ChatListAdapter;
import com.gabiq.simplechat.models.Message;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class ChatActivity extends Activity {
    private static final String TAG = ChatActivity.class.getName();
    private static String sUserId;

    private static final int MAX_CHAT_MESSAGES_TO_SHOW = 50;
    
    public static final String USER_ID_KEY = "userId";

    private EditText etMessage;
    private Button btSend;
    
    private ListView lvChat;
    private ArrayList<Message> mMessages;
    private ChatListAdapter mAdapter;
    
 // Create a handler which can run code periodically
    private Handler handler = new Handler();


    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        if (ParseUser.getCurrentUser() != null) {
            startWithCurrentUser();
        } else {
            login();
        }
        // Run the runnable object defined every 100ms
        handler.postDelayed(runnable, 100);
    }
    
    
 // Setup message field and posting
    private void setupMessagePosting() {
            etMessage = (EditText) findViewById(R.id.etMessage);
            btSend = (Button) findViewById(R.id.btSend);
            lvChat = (ListView) findViewById(R.id.lvChat);
            mMessages = new ArrayList<Message>();
            mAdapter = new ChatListAdapter(ChatActivity.this, sUserId, mMessages);
            lvChat.setAdapter(mAdapter);
            btSend.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    String body = etMessage.getText().toString();
                    // Use Message model to create new messages now      
                    Message message = new Message();
                    message.setUserId(sUserId);
                    message.setBody(body);
                    message.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            receiveMessage();
                        }
                    });
                    etMessage.setText("");
                }
        });
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

 // Query messages from Parse so we can load them into the chat adapter
    private void receiveMessage() {
                    // Construct query to execute
            ParseQuery<Message> query = ParseQuery.getQuery(Message.class);
            query.setLimit(MAX_CHAT_MESSAGES_TO_SHOW);
            query.orderByAscending("createdAt");
                    // Execute query for messages asynchronously
            query.findInBackground(new FindCallback<Message>() {
                public void done(List<Message> messages, ParseException e) {
                    if (e == null) {                    
                        mMessages.clear();
                        mMessages.addAll(messages);
                        mAdapter.notifyDataSetChanged();
                        lvChat.invalidate();
                    } else {
                        Log.d("message", "Error: " + e.getMessage());
                    }
                }
            });
    }
    
 // Defines a runnable which is run every 100ms
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
           refreshMessages();
           handler.postDelayed(this, 100);
        }
    };

    private void refreshMessages() {
        receiveMessage();       
    }
}