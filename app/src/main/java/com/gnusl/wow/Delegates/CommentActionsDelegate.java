package com.gnusl.wow.Delegates;

import com.gnusl.wow.Models.Comment;

public interface CommentActionsDelegate {

    public void onEditComment(Comment comment);

    public void onDeleteComment(Comment comment);
}
