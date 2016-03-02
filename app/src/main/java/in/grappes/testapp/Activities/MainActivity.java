package in.grappes.testapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.tumblr.jumblr.JumblrClient;
import com.tumblr.jumblr.types.Blog;
import com.tumblr.jumblr.types.User;

import java.util.ArrayList;
import java.util.List;

import in.grappes.testapp.ASFObjectStore;
import in.grappes.testapp.App;
import in.grappes.testapp.R;
import in.grappes.testapp.RecyclerClickListener;
import in.grappes.testapp.adapters.BlogAdapter;

public class MainActivity extends AppCompatActivity {

    ProgressDialog loadingDialog;
    RecyclerView blogRecyclerView;
    BlogAdapter blogAdapter;
    List<Blog> blogList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadingDialog = new ProgressDialog(MainActivity.this);
        loadingDialog.setMessage("Loading...");

        initializeRecyclerView();

    }

    @Override
    public void onResume()
    {
        super.onResume();
        AsyncTaskRunner runner = new AsyncTaskRunner();
        runner.execute("");

    }

    private void initializeRecyclerView() {
        blogRecyclerView = (RecyclerView) findViewById(R.id.blog_recycler_view);
        blogRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        blogRecyclerView.setLayoutManager(layoutManager);

        blogRecyclerView.addOnItemTouchListener(new RecyclerClickListener(MainActivity.this, new RecyclerClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                String blogObject = ASFObjectStore.getDefault().pushWeak(blogList.get(position));
                Intent i = new Intent(MainActivity.this, PostActivity.class);
                i.putExtra("blog",blogObject);
                startActivity(i);
            }
        }));
    }


    private class AsyncTaskRunner extends AsyncTask<String, String, String> {
        private String resp;
        @Override
        protected String doInBackground(String... params) {
            User user = App.client.user();
            Log.d("test", user.getName());
            blogList.addAll(user.getBlogs());
            blogAdapter = new BlogAdapter(user.getBlogs(),MainActivity.this,blogRecyclerView);
            return resp;
        }

        @Override
        protected void onPostExecute(String result) {
            blogRecyclerView.setAdapter(blogAdapter);
            loadingDialog.dismiss();
        }
        @Override
        protected void onPreExecute() {
            loadingDialog.show();
        }

        @Override
        protected void onProgressUpdate(String... text) {

        }
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if(isFinishing())
        {
            loadingDialog.dismiss();
        }
    }
}
