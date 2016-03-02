package in.grappes.testapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tumblr.jumblr.types.Blog;

import java.util.ArrayList;
import java.util.List;

import in.grappes.testapp.R;
import in.grappes.testapp.ViewHolders.BlogViewHolder;

/**
 * Created by GunjitDhawan on 3/1/2016.
 */
public class BlogAdapter extends RecyclerView.Adapter<BlogViewHolder>{
    
    List<Blog> blogList = new ArrayList<>();
    Context context;

    public BlogAdapter(List<Blog> blogList, final Context context, RecyclerView recyclerView) {
        this.context = context;
        this.blogList = blogList;
    }

    @Override
    public BlogViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.blog_row_view, viewGroup, false);
        return new BlogViewHolder(view);

    }

    @Override
    public void onBindViewHolder(BlogViewHolder blogViewHolder, int i) {
        Blog blog = blogList.get(i);

        blogViewHolder.blogTitle.setText(blog.getTitle());
        blogViewHolder.numberOfPosts.setText("Posts : "+String.valueOf(blog.getPostCount()));
        blogViewHolder.author.setText("- "+blog.getName());
    }

    @Override
    public int getItemCount() {
        return blogList.size();
    }
}
