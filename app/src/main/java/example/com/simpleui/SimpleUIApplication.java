package example.com.simpleui;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by tomcat on 2015/12/14.
 */
public class SimpleUIApplication extends Application {
    public void onCreate()
    {
        super.onCreate();

        Parse.enableLocalDatastore(this);
        Parse.initialize(this);

    }
}
