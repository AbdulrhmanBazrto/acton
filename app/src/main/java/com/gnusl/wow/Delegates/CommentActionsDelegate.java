package com.gnusl.wow.Delegates;

import com.gnusl.wow.Models.Comment;

public interface CommentActionsDelegate {

    void onEditComment(Comment comment);

    void onDeleteComment(Comment comment);
}
