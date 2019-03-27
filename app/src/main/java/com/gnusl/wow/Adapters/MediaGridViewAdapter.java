package com.gnusl.wow.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gnusl.wow.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MediaGridViewAdapter extends ArrayAdapter<String> {

    private Context mContext;
    private List<String> mediaFilePathList;
    LayoutInflater viewInflater;
    private int mWidth;


    public MediaGridViewAdapter(Context context, int resource, List<String> filePathList) {
        super(context, resource, filePathList);
        mediaFilePathList = filePathList;
        mContext = context;
        viewInflater = LayoutInflater.from(mContext);
    }

    public int getCount() {
        return mediaFilePathList.size() == 0 ? 0 : 1;
    }

    @Override
    public String getItem(int position) {
        return mediaFilePathList.get(position);
    }


    public void addAll(List<String> mediaFile) {
        if (mediaFile != null) {
            int count = mediaFile.size();
            for (int i = 0; i < count; i++) {
                if (!mediaFilePathList.contains(mediaFile.get(i))) {
                    mediaFilePathList.add(mediaFile.get(i));
                }
            }
        }
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {

            mWidth = mContext.getResources().getDisplayMetrics().widthPixels;
            convertView = viewInflater.inflate(R.layout.view_grid_item, parent, false);
            holder = new ViewHolder();
            holder.imageView = convertView.findViewById(R.id.imageViewFromGridItemRowView);
            holder.nameTextView = convertView.findViewById(R.id.nameTextView);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        RelativeLayout.LayoutParams imageParams = (RelativeLayout.LayoutParams) holder.imageView.getLayoutParams();
        imageParams.width = mWidth / 2;
        imageParams.height = mWidth / 2;

        holder.imageView.setLayoutParams(imageParams);

        File mediaFile = new File(mediaFilePathList.get(position));

        if (mediaFile.exists()) {
            if (mediaFile.getPath().contains("mp4") || mediaFile.getPath().contains("wmv") ||
                    mediaFile.getPath().contains("avi") || mediaFile.getPath().contains("3gp")) {
                holder.imageView.setImageBitmap(null);

                int sdk = android.os.Build.VERSION.SDK_INT;
                if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    holder.imageView.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.ic_video_selected_from_media_chooser_header_bar));
                } else {
                    holder.imageView.setBackground(mContext.getResources().getDrawable(R.drawable.ic_video_selected_from_media_chooser_header_bar));
                }

            } else {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPurgeable = true;
                options.inSampleSize = 2;
                Bitmap myBitmap = BitmapFactory.decodeFile(mediaFile.getAbsolutePath(), options);
                holder.imageView.setImageBitmap(myBitmap);
            }

            holder.nameTextView.setText(mediaFile.getName());

        }

        return convertView;
    }

    class ViewHolder {
        ImageView imageView;
        TextView nameTextView;
    }

    public List<String> getMediaFilePathList() {
        return mediaFilePathList;
    }

    public void setMediaFilePathList(List<String> mediaFilePathList) {
        this.mediaFilePathList = mediaFilePathList;
    }

    public ArrayList<Bitmap> getImages() {

        ArrayList<Bitmap> images = new ArrayList<>();

        for (String s : mediaFilePathList) {
            File mediaFile = new File(s);
            if (mediaFile.exists())
                if (!mediaFile.getPath().contains("mp4") && !mediaFile.getPath().contains("wmv") ||
                        !mediaFile.getPath().contains("avi") && !mediaFile.getPath().contains("3gp")) {

                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPurgeable = true;
                    options.inSampleSize = 2;
                    images.add(BitmapFactory.decodeFile(mediaFile.getAbsolutePath(), options));
                }
        }
        return images;
    }

    public ArrayList<File> getImagesAsFiles() {

        ArrayList<File> files = new ArrayList<>();

        for (String s : mediaFilePathList) {
            File mediaFile = new File(s);
            if (mediaFile.exists())
                if (!mediaFile.getPath().contains("mp4") && !mediaFile.getPath().contains("wmv") ||
                        !mediaFile.getPath().contains("avi") && !mediaFile.getPath().contains("3gp")) {
                    files.add(mediaFile);
                }
        }
        return files;
    }

    public File getVideo() {


        for (String s : mediaFilePathList) {
            File mediaFile = new File(s);
            if (mediaFile.exists())
                if (mediaFile.getPath().contains("mp4") || mediaFile.getPath().contains("wmv") ||
                        mediaFile.getPath().contains("avi") || mediaFile.getPath().contains("3gp"))
                    return mediaFile;
        }
        return null;
    }

}
