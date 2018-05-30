package com.woystech.net.application;

import android.app.Application;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.woystech.net.BundleKey;
import com.woystech.net.cache.BitmapLruCache;
import com.woystech.net.connectivity.NetworkReceiver;
import com.woystech.net.connectivity.NetworkReceiverListener;

/**
 * Created by firok on 7/23/2016.
 */
public class AppController extends Application {

    public static final String TAG = AppController.class.getSimpleName();

    private RequestQueue requestQueue;

    private ImageLoader imageLoader;

    private static AppController instance;

    //user infos will be stored in sharedPreferences
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static synchronized AppController getInstance() {
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null)
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        return requestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (imageLoader == null)
            imageLoader = new ImageLoader(this.requestQueue, BitmapLruCache.getInstance());
        return imageLoader;
    }

    /**
     * add to request queue
     *
     * @param request
     * @param tag
     * @param <T>
     */
    public <T> void addToRequestQueue(Request<T> request, String tag) {
        //set the default tag if the tag is empty
        request.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(request);
    }

    /**
     * add to request queue with null tag
     *
     * @param request
     * @param <T>
     */
    public <T> void addToRequestQueue(Request<T> request) {
        addToRequestQueue(request, null);
    }

    /**
     * cancel pending requests
     *
     * @param tag
     */
    public void cancelPendingRequests(Object tag) {
        if (requestQueue != null)
            requestQueue.cancelAll(tag);
    }

    public void setNetworkListener(NetworkReceiverListener listener){
        NetworkReceiver.networkReceiverListener = listener;
    }

    /**
     * this method gets the sharedPreferences
     * @return
     */
    public SharedPreferences getSharedPreferences(){
        if(sharedPreferences == null){
            sharedPreferences = getSharedPreferences(BundleKey.SHARED_PREFERENCES_INFO, MODE_PRIVATE);
        }
        return sharedPreferences;
    }

    /**
     * clear the sharedPreference on logout
     */
    public void logout(){
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.clear();
        editor.apply();
    }

    public void userLogin(long id, String name, String email){
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putLong(BundleKey.EDITOR_USER_ID, id);
        editor.putString(BundleKey.EDITOR_USER_NAME,name);
        editor.putString(BundleKey.EDITOR_USER_EMAIL,email);
        editor.putBoolean(BundleKey.EDITOR_IS_LOGGED_IN,true);
        editor.apply();
    }

    public boolean isLoggedIn(){
        return getSharedPreferences().getBoolean(BundleKey.EDITOR_IS_LOGGED_IN,false);
    }
}
