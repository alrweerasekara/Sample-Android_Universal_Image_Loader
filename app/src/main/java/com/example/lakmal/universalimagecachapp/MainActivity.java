package com.example.lakmal.universalimagecachapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.impl.LimitedAgeDiskCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;
import com.nostra13.universalimageloader.utils.StorageUtils;

import org.joda.time.Interval;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements ImageListAdapter.ImageItemCallback {

    private RecyclerView rvImageList;
    private DisplayImageOptions options;
    private ImageListAdapter rvImageListAdapter;

    private String[] list = new String[]{"https://4.bp.blogspot.com/-k8IX2Mkf0Jo/U4ze2gNn7CI/AAAAAAAA7xg/iKxa6bYITDs/s0/HOT+Balloon+Trip_Ultra+HD.jpg",
            "http://gallery.dralzheimer.stylesyndication.de/galleries/wallpaper/Teufelsburg-8K-Wallpaper.jpg",
            "https://lh5.googleusercontent.com/-7qZeDtRKFKc/URquWZT1gOI/AAAAAAAAAbs/hqWgteyNXsg/s1024/Another%252520Rockaway%252520Sunset.jpg",
            "http://fmdev.ivivacloud.com/Attachments/Download?AttachmentKey=245",
            "http://fmdev.ivivacloud.com/AccountResources/SmartFM/5_5BA0C5A3-4EDB-4489-9401-E380569DC0FD_1472446490.06831.jpg",
            "http://cdn.wallpapersafari.com/32/60/Ke9IoD.jpg"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rvImageList = (RecyclerView) findViewById(R.id.image_list);

        /*options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_empty)
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();*/

        List<String> imageList = Arrays.asList(list);
        rvImageListAdapter = new ImageListAdapter(this, imageList, this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvImageList.setLayoutManager(layoutManager);
        rvImageList.setHasFixedSize(true);
        rvImageList.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        rvImageList.setItemAnimator(new DefaultItemAnimator());
        rvImageList.setAdapter(rvImageListAdapter);
    }

    @Override
    public void onClickImageItem(String url) {
        Intent intent = new Intent(this, FullViewActivity.class);
        intent.putExtra("URL", url);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.act_clear_disk:
                removeMemoryCache();
                return true;
            case R.id.act_clear_memory:

                return true;
            case R.id.act_clear_disk_min:
                removeDiskCacheBefore1Min();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void removeMemoryCache() {
        ImageLoader.getInstance().clearDiskCache();
        Log.e("L-IMG", "Disk Cleared");
    }

    private void removeDiskCacheBefore1Min() {
        File[] lakmalTestCaches = StorageUtils.getOwnCacheDirectory(getApplicationContext(), "LakmalTestCache").listFiles();
        for (File file : lakmalTestCaches) {
            long modified = file.lastModified();//milliseconds
            long current = Calendar.getInstance(Locale.getDefault()).getTime().getTime();//milliseconds
            Interval interval = new Interval(modified, current);
            if (interval.toPeriod().getDays()>7) {
                File forceDel = new File(file.getPath());
                if (forceDel.exists()) {
                    forceDel.delete();
                }
            }
        }
        Log.e("L-IMG", "Disk Cleared before 1 minute");
    }
}
