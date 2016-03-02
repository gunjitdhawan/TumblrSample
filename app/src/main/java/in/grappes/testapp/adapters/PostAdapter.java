package in.grappes.testapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tumblr.jumblr.types.Blog;
import com.tumblr.jumblr.types.Post;
import com.tumblr.jumblr.types.TextPost;

import java.util.ArrayList;
import java.util.List;

import in.grappes.testapp.Activities.PostActivity;
import in.grappes.testapp.R;
import in.grappes.testapp.ViewHolders.BlogViewHolder;
import in.grappes.testapp.ViewHolders.PostViewHolder;

/**
 * Created by GunjitDhawan on 3/1/2016.
 */
public class PostAdapter  extends RecyclerView.Adapter<PostViewHolder>
{
    List<Post> postList = new ArrayList<>();
    Context context;

    public PostAdapter(List<Post> postList, PostActivity context, RecyclerView postRecyclerView) {
        this.postList = postList;
        this.context = context;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.post_row_view, viewGroup, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PostViewHolder postViewHolder, int i) {
        postViewHolder.postTextTitle.setText(((TextPost) postList.get(i)).getTitle());
        postViewHolder.postTextBody.setText(Html.fromHtml(((TextPost) postList.get(i)).getBody()).toString());
        postViewHolder.postStatus.setText("Status : "+postList.get(i).getState());
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }
}
