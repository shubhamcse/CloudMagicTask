package com.shubham.cloudmagictask;

import android.app.Application;
import android.content.Context;

import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;

/**
 * Created by Shubham Gupta on 04-10-2015.
 */
public class MyApplication extends Application {
    private static MyApplication instance;
    public MyApplication(){
        instance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //Iconify.with(new MaterialModule());
    }

    public static OkHttpClient client;

    public static void initCache(File cacheDirectory) throws Exception {
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(cacheDirectory, cacheSize);
        client = new OkHttpClient();
        client.setCache(cache);

    }


    public static Context getContext() {
        return instance;
    }
}
