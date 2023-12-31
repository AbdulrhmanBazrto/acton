package com.gnusl.wow.Adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.gnusl.wow.Connection.APIConnectionNetwork;
import com.gnusl.wow.Connection.APILinks;
import com.gnusl.wow.Delegates.CommentActionsDelegate;
import com.gnusl.wow.Delegates.ConnectionDelegate;
import com.gnusl.wow.Models.Comment;
import com.gnusl.wow.R;
import com.gnusl.wow.Views.AutoFitFontedTextView;
import com.klinker.android.sliding.TouchlessScrollView;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;

public class CommentsRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Comment> comments;
    private boolean isLoading = false;
    private CommentActionsDelegate commentActionsDelegate;

    private static int COMMENT_HOLDER = 0;
    private static int LOAD_MORE_HOLDER = 1;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public CommentsRecyclerViewAdapter(Context context, List<Comment> comments,CommentActionsDelegate commentActionsDelegate) {
        this.context = context;
        this.comments = comments;
        this.commentActionsDelegate=commentActionsDelegate;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;

        view = inflater.inflate(R.layout.comment_view_holder, parent, false);
        return new CommentViewHolder(view);


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ((CommentViewHolder) holder).bind(comments.get(position), position);
    }

    @Override
    public int getItemCount() {
        return isLoading ? comments.size() + 1 : comments.size();
    }

    @Override
    public int getItemViewType(int position) {

        return COMMENT_HOLDER;
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {

        private CircularImageView profile_image;
        private AutoFitFontedTextView user_name;
        private AutoFitFontedTextView msg;
        private AutoFitFontedTextView date;
        private RecyclerView tags_recycler_view;


        public CommentViewHolder(View itemView) {
            super(itemView);

            profile_image = itemView.findViewById(R.id.profile_image);
            user_name = itemView.findViewById(R.id.user_name);
            msg = itemView.findViewById(R.id.msg);
            date = itemView.findViewById(R.id.date);
            tags_recycler_view = itemView.findViewById(R.id.tags_recycler);
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
        public void bind(final Comment comment, final int position) {

            // change orintation
           // itemView.setLayoutDirection(position % 2 == 0 ? View.LAYOUT_DIRECTION_LTR : View.LAYOUT_DIRECTION_RTL);

            // name
            user_name.setText(comment.getUser().getName());

            // image
            Glide.with(context)
                    .load(comment.getUser().getImage_url())
                    .into(profile_image);

            // comment msg
            msg.setText(comment.getComment());

            // date
            date.setText(comment.getCreatedAt().split(" ")[0]);


            itemView.setOnLongClickListener(v->{

                PopupMenu dropDownMenu = new PopupMenu(context,itemView);
                dropDownMenu.getMenuInflater().inflate(R.menu.comment_menu_option, dropDownMenu.getMenu());
                dropDownMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        switch(menuItem.getItemId()){

                            case R.id.edit_comment:

                                commentActionsDelegate.onEditComment(comment);
                                break;

                            case R.id.delete_comment:

                                commentActionsDelegate.onDeleteComment(comment);
                                break;
                        }

                        return true;
                    }
                });
                dropDownMenu.show();

            return true;});

        }

    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
        notifyDataSetChanged();
    }

    public void update(List<Comment> list) {

        int position = comments.size();

        this.comments.addAll(list);

        notifyItemInserted(position);
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;

        notifyDataSetChanged();
        // notifyItemChanged(posts.size());

    }

}

