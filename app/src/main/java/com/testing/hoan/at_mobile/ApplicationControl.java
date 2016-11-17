package com.testing.hoan.at_mobile;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.LruCache;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by Hoan on 9/11/2016.
 */
public class ApplicationControl extends Application{
    public static final String TAG= ApplicationControl.class.getSimpleName();
    private RequestQueue mRequestQueue;
    private static ApplicationControl mInstance;
    private ImageLoader mImageLoader;
    private Context context;

    //@Override
    //public void onCreate() {
      //  super.onCreate();
       // mInstance=this;
   // }
    public ApplicationControl(Context context){
        this.context=context;
        this.mRequestQueue=getmRequestQueue();
        mImageLoader=new ImageLoader(mRequestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String,Bitmap>
                    cache=new LruCache<String,Bitmap>(20);
                    @Override
                    public Bitmap getBitmap(String s) {
                        return cache.get(s);
                    }

                    @Override
                    public void putBitmap(String s, Bitmap bitmap) {
                         cache.put(s,bitmap);
                    }
                });
    }
    public static synchronized ApplicationControl getInstance(Context context){
        if(mInstance==null){
            mInstance=new ApplicationControl(context);
        }
        return mInstance;
    }
    public RequestQueue getmRequestQueue() {
        if (mRequestQueue == null) {
            Cache cache = new DiskBasedCache(context.getCacheDir(), 10 * 1024 * 1024);
            Network network = new BasicNetwork(new HurlStack());
            mRequestQueue = new RequestQueue(cache, network);
            mRequestQueue.start();
        }
        return mRequestQueue;
    }
    public <T> void addToRequestQueue(Request<T> req,String tag){
        req.setTag(TextUtils.isEmpty(tag)? TAG :tag);
        getmRequestQueue().add(req);
    }
    public <T> void addToRequestQueue(Request<T> req){
        getmRequestQueue().add(req);
    }
    public void cancelPendingRequest(Object tag){
        if(mRequestQueue!=null){
            mRequestQueue.cancelAll(tag);
        }
    }
    public ImageLoader getmImageLoader(){
        return this.mImageLoader;
    }
}
