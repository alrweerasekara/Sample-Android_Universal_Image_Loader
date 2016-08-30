package com.example.lakmal.universalimagecachapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by lakmal on 8/29/16.
 */
public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.ImageHolder> {

    private List<String> mImageList;
    private Context mContext;
    private DisplayImageOptions mImageOptions;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    private ImageItemCallback mDelegate;

    public ImageListAdapter(Context context, List<String> imageList, ImageItemCallback delegate) {
        this.mContext = context;
        this.mImageList = imageList;
        this.mDelegate = delegate;
        mImageOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_empty)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .imageScaleType(ImageScaleType.EXACTLY)
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    @Override
    public ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
        return new ImageHolder(row);
    }

    @Override
    public void onBindViewHolder(final ImageHolder holder, int position) {
        String item = mImageList.get(position);
        //ImageLoader.getInstance().displayImage(item,holder.image,mImageOptions);
        ImageLoader.getInstance()
                .displayImage(item, holder.image, mImageOptions, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        holder.progressBar.setProgress(0);
                        holder.progressBar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        holder.progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        holder.progressBar.setVisibility(View.GONE);
                    }
                }, new ImageLoadingProgressListener() {
                    @Override
                    public void onProgressUpdate(String imageUri, View view, int current, int total) {
                        holder.progressBar.setProgress(Math.round(100.0f * current / total));
                        Log.v("elemele","onProgressUpdate ############################### " + current + "/" + total);
                    }
                });
    }

    @Override
    public int getItemCount() {
        return mImageList.size();
    }

    public class ImageHolder extends RecyclerView.ViewHolder implements OnClickListener{
        ProgressBar progressBar;
        private final ImageView image;

        public ImageHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progress);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mDelegate.onClickImageItem(mImageList.get(getAdapterPosition()));
        }
    }

    public interface ImageItemCallback {
        void onClickImageItem(String url);
    }


    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }
}
