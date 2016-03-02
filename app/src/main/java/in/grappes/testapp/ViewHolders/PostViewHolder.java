package in.grappes.testapp.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import in.grappes.testapp.R;

/**
 * Created by GunjitDhawan on 3/1/2016.
 */
public class PostViewHolder extends RecyclerView.ViewHolder {

    public TextView postTextTitle;
    public TextView postTextBody;
    public TextView postStatus;
    public PostViewHolder(View itemView) {
        super(itemView);
        postTextTitle = (TextView) itemView.findViewById(R.id.post_text_title);
        postTextBody = (TextView) itemView.findViewById(R.id.post_text_body);
        postStatus = (TextView) itemView.findViewById(R.id.post_status);
    }
}
