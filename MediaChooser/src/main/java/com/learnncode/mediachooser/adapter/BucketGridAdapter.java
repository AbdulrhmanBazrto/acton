
package com.learnncode.mediachooser.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.learnncode.mediachooser.R;
import com.learnncode.mediachooser.Utilities.BucketEntry;
import com.learnncode.mediachooser.Utilities.MediaChooserConstants;
import com.learnncode.mediachooser.async.ImageLoadAsync;
import com.learnncode.mediachooser.async.MediaAsync;
import com.learnncode.mediachooser.async.VideoLoadAsync;
import com.learnncode.mediachooser.fragment.BucketVideoFragment;

import java.util.ArrayList;


/*
 * Copyright 2015 - learnNcode (learnncode@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

public class BucketGridAdapter extends ArrayAdapter<BucketEntry> {

    public BucketVideoFragment bucketVideoFragment;

    private Context mContext;
    private ArrayList<BucketEntry> mBucketEntryList;
    private boolean mIsFromVideo;
    private int mWidth;
    private LayoutInflater mViewInflater;


    public BucketGridAdapter(Context context, ArrayList<BucketEntry> categories, boolean isFromVideo) {

        super(context, 0, categories);
        mBucketEntryList = categories;
        mContext = context;
        mIsFromVideo = isFromVideo;
        mViewInflater = LayoutInflater.from(mContext);
    }

    public int getCount() {
        return mBucketEntryList.size();
    }

    @Override
    public BucketEntry getItem(int position) {
        return mBucketEntryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addLatestEntry(String url) {
        int count = mBucketEntryList.size();
        boolean success = false;
        for (int i = 0; i < count; i++) {
            if (mBucketEntryList.get(i).bucketName.equals(MediaChooserConstants.folderName)) {
                mBucketEntryList.get(i).bucketUrl = url;
                success = true;
                break;
            }
        }

        if (!success) {
            BucketEntry latestBucketEntry = new BucketEntry(0, MediaChooserConstants.folderName, url);
            mBucketEntryList.add(0, latestBucketEntry);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {

            mWidth = mContext.getResources().getDisplayMetrics().widthPixels;

            convertView = mViewInflater.inflate(R.layout.view_grid_bucket_item_media_chooser, parent, false);

            holder = new ViewHolder();
            holder.imageView = convertView.findViewById(R.id.imageViewFromMediaChooserBucketRowView);
            holder.nameTextView = convertView.findViewById(R.id.nameTextViewFromMediaChooserBucketRowView);

            FrameLayout.LayoutParams imageParams = (FrameLayout.LayoutParams) holder.imageView.getLayoutParams();
            imageParams.width = mWidth / 2;
            imageParams.height = mWidth / 2;

            holder.imageView.setLayoutParams(imageParams);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        if (mIsFromVideo) {
            new VideoLoadAsync(bucketVideoFragment, holder.imageView, false, mWidth / 2).executeOnExecutor(MediaAsync.THREAD_POOL_EXECUTOR, mBucketEntryList.get(position).bucketUrl);

        } else {
            ImageLoadAsync loadAsync = new ImageLoadAsync(mContext, holder.imageView, mWidth / 2);
            loadAsync.executeOnExecutor(MediaAsync.THREAD_POOL_EXECUTOR, mBucketEntryList.get(position).bucketUrl);
        }

        holder.nameTextView.setText(mBucketEntryList.get(position).bucketName);
        return convertView;
    }

    class ViewHolder {
        ImageView imageView;
        TextView nameTextView;
    }
}


