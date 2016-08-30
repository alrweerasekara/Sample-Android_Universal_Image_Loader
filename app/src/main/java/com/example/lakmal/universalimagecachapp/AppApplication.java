package com.example.lakmal.universalimagecachapp;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

/**
 * Created by lakmal on 8/26/16.
 */
public class AppApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        iniImageLoader(getApplicationContext());
    }

    public static void iniImageLoader(Context context) {
        File cachDir = StorageUtils.getOwnCacheDirectory(context, "LakmalTestCache");
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCache(new UnlimitedDiskCache(cachDir));
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        //config.diskCacheSize(50 * 1024 * 1024);
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs();

        ImageLoader.getInstance().init(config.build());

    }
}
