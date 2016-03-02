package in.grappes.testapp.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import in.grappes.testapp.R;

/**
 * Created by GunjitDhawan on 3/1/2016.
 */
public class BlogViewHolder extends RecyclerView.ViewHolder {

    public TextView blogTitle;
    public TextView numberOfPosts;
    public TextView author;

    public BlogViewHolder(View itemView) {
        super(itemView);

        blogTitle = (TextView) itemView.findViewById(R.id.blog_title);
        numberOfPosts = (TextView) itemView.findViewById(R.id.blog_number_of_posts);
        author = (TextView) itemView.findViewById(R.id.blog_author);

    }


}
