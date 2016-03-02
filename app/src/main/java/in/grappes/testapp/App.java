package in.grappes.testapp;

import android.app.Application;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.tumblr.jumblr.JumblrClient;
import com.tumblr.jumblr.types.Blog;
import com.tumblr.jumblr.types.User;

/**
 * Created by GunjitDhawan on 2/29/2016.
 */
public class App extends Application{
    public static JumblrClient client;

    @Override
    public void onCreate()
    {
        super.onCreate();
        client = new JumblrClient(
                "FfvZLrd8yQQBNYVYcVyYHjlR5pcdEaCECiDEmaOtylmtVeXbZr",
                "zqk8qRhARIO3I7b993ftjyhXohQAx58H5GsWU1tyQxd4bbo9XX"
        );

        client.setToken(
                "xilcucgeZ11Ko4MCx76VGhuMps00LuVCQYF4sGhPhZJ6B0l8UQ",
                "Z53pTtKDJVWLuYUQwNL69rzQUE3UAVZ7Aij5l9Z9UAkkii5SQ6"
        );



    }


}

