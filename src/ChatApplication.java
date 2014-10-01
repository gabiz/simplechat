import android.app.Application;

import com.parse.Parse;


public class ChatApplication extends Application {
    public static final String YOUR_APPLICATION_ID = "QY1YDlP7oSnz9l5FuRChPYCHrAzZcE4tc12uiCNW";
    public static final String YOUR_CLIENT_KEY = "xSrwaefRGYgN1Y4Dn7PvVhVUYkpxOrvOgYcagGxG";
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, YOUR_APPLICATION_ID, YOUR_CLIENT_KEY);
    }
}
