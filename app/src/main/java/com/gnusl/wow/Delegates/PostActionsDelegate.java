package com.gnusl.wow.Delegates;

import com.gnusl.wow.Models.FeaturePost;

public interface PostActionsDelegate {

    public void onEditPost(FeaturePost post);

    public void onDeletePost(FeaturePost post);
}
