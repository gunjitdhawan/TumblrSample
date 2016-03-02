package in.grappes.testapp.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tumblr.jumblr.types.Blog;
import com.tumblr.jumblr.types.Post;
import com.tumblr.jumblr.types.TextPost;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import in.grappes.testapp.ASFObjectStore;
import in.grappes.testapp.App;
import in.grappes.testapp.R;
import in.grappes.testapp.RecyclerClickListener;
import in.grappes.testapp.adapters.PostAdapter;

public class PostActivity extends AppCompatActivity {


    ProgressDialog loadingDialog;
    Blog blogObject;
    RecyclerView postRecyclerView;
    PostAdapter postAdapter;
    List<Post> postList = new ArrayList<>();

    String SEPERATOR  = "---x---";

    TextView addNewPostButton;
    String MTD = "Move to drafts";
    String PUBLISH = "Publish";
    String DELETE = "Delete";
    final String[] items = {
            MTD,PUBLISH,DELETE
    };

    long selectedPostId=0;
    String selectedOperation="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String myObjectKey = getIntent().getStringExtra("blog");
        blogObject = (Blog) ASFObjectStore.getDefault().pop(myObjectKey);

        initializeRecyclerView();
        initializeViews();

        executeBackgroundTask("");

    }

    private void initializeViews() {
        loadingDialog = new ProgressDialog(PostActivity.this);
        loadingDialog.setMessage("Loading...");

        addNewPostButton = (TextView) findViewById(R.id.add_new_post_button);
        addNewPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddPostDialog();
            }
        });

    }

    private void showAddPostDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_post_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText postTextTitle = (EditText) dialogView.findViewById(R.id.post_title_edit_text);
        final EditText postTextBody = (EditText) dialogView.findViewById(R.id.post_body_edit_text);
        dialogBuilder.setCancelable(false);
        dialogBuilder.setTitle("Add a new post");
        dialogBuilder.setPositiveButton("Post", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                if (validateForm(postTextTitle, postTextBody)) {
                    executeBackgroundTask(postTextTitle.getText().toString() + SEPERATOR + postTextBody.getText().toString());
                }
            }
        });
        dialogBuilder.setNegativeButton("Discard", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    private boolean validateForm(EditText postTextTitle, EditText postTextBody) {
        if(TextUtils.isEmpty(postTextTitle.getText().toString())) {
            postTextTitle.setError("Title cannot be empty!");
            return false;
        }
        else if(TextUtils.isEmpty(postTextBody.getText().toString())) {
            postTextBody.setError("Body cannot be empty!");
            return false;
        }
        else {
            return true;
        }

    }

    public void showChangeStatusDialog(final Post post)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {
                if (items[i].equalsIgnoreCase(MTD)) {
                    selectedOperation = "drafts";
                    executeBackgroundTask(String.valueOf(post.getId()));
                } else if (items[i].equalsIgnoreCase(PUBLISH)) {
                    selectedOperation = "published";
                    executeBackgroundTask(String.valueOf(post.getId()));
                } else {
                    selectedOperation = "delete";
                    executeBackgroundTask(String.valueOf(post.getId()));
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void executeBackgroundTask(String data) {
        Log.d("DATA",data);
        AsyncTaskRunner runner = new AsyncTaskRunner();
        runner.execute(data);
    }

    private void initializeRecyclerView() {
        postRecyclerView = (RecyclerView) findViewById(R.id.post_recycler_view);
        postRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        postRecyclerView.setLayoutManager(layoutManager);


        postRecyclerView.addOnItemTouchListener(new RecyclerClickListener(PostActivity.this, new RecyclerClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                showChangeStatusDialog(postList.get(position));
                selectedPostId = postList.get(position).getId();
            }
        }));
    }


    private class AsyncTaskRunner extends AsyncTask<String, String, String> {
        private String resp;
        @Override
        protected String doInBackground(String... params) {

            if(!params[0].isEmpty() && params[0].contains(SEPERATOR)) {
                Log.d("title", params[0].split(SEPERATOR)[0] + " " + SEPERATOR);

                String title = params[0].split(SEPERATOR)[0];
                String body = params[0].split(SEPERATOR)[1];
                TextPost tp = null;
                try {
                    tp = App.client.newPost(blogObject.getName(), TextPost.class);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
                tp.setTitle(title);
                tp.setBody(body);
                tp.save();
                executeBackgroundTask("");
            }
            else if(!params[0].isEmpty() && !params[0].contains(SEPERATOR))
            {
                Post tp = App.client.blogPost(blogObject.getName(),selectedPostId);
                if(tp!=null && selectedOperation.contains("draft"))
                {
                    Log.d("STATUS", "MOVING TO DRAFT");
                    tp.setState("draft");
                    Log.d("STATUS", blogObject.getPost(selectedPostId).getState());
                }
                else if(tp!=null && selectedOperation.contains("published"))
                {
                    Log.d("STATUS", "PUBLISHING");
                    tp.setState("published");

                }
                else
                {
                    Log.d("STATUS","DELETING");
                    tp.delete();
                }
                try {
                    tp.save();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                executeBackgroundTask("");
            }
            else {
                postList = new ArrayList<>();
                postList.addAll(blogObject.posts());
                Log.d("post_number", String.valueOf(blogObject.posts().size()));
                postAdapter = new PostAdapter(postList, PostActivity.this, postRecyclerView);
            }

            return resp;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("POSTED", "SUCCESS");

            postRecyclerView.setAdapter(postAdapter);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
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
