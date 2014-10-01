package com.gabiq.simplechat;
import android.app.Application;

import com.gabiq.simplechat.models.Message;
import com.parse.Parse;
import com.parse.ParseObject;


public class ChatApplication extends Application {
    public static final String YOUR_APPLICATION_ID = "QY1YDlP7oSnz9l5FuRChPYCHrAzZcE4tc12uiCNW";
    public static final String YOUR_CLIENT_KEY = "xSrwaefRGYgN1Y4Dn7PvVhVUYkpxOrvOgYcagGxG";
    @Override
    public void onCreate() {
        super.onCreate();
        // Register your parse models here
        ParseObject.registerSubclass(Message.class);
        // Existing initialization happens after all classes are registered
        Parse.initialize(this, YOUR_APPLICATION_ID, YOUR_CLIENT_KEY);
    }
}
