package com.gnusl.wow.Delegates;

import com.gnusl.wow.Models.FeaturePost;

public interface PostActionsDelegate {

    void onEditPost(FeaturePost post);

    void onDeletePost(FeaturePost post);
}
