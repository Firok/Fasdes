package com.woystech.fasdes.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.woystech.common.ApplicationConstants;
import com.woystech.fasdes.R;
import com.woystech.fasdes.models.Client;
import com.woystech.fasdes.net.Endpoint;
import com.woystech.net.BundleKey;
import com.woystech.net.NetworkRequestListener;
import com.woystech.net.NetworkUtilsImpl;
import com.woystech.net.application.AppController;
import com.woystech.net.connectivity.NetworkReceiver;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by firok on 2/13/2017.
 */

public class SplashActivity extends AppCompatActivity {

    private static final String TAG =  SplashActivity.class.getSimpleName();

    private String endpoint;
    private Map<String, String> urlParams;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Thread timer = new Thread(){
            public void run(){
                try {
                    sleep(1000);
                }
                catch (InterruptedException ie){
                    ie.printStackTrace();
                }
                finally {
                    if (!AppController.getInstance().isLoggedIn()) {
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        finish();
                    }
                    else {
                        checkConnection();
                        final Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                        SharedPreferences sharedPreferences = AppController.getInstance().getSharedPreferences();
                        long userId = sharedPreferences.getLong(BundleKey.EDITOR_USER_ID, -1);
                        initEndpointAndUrlParam(userId);
                        try{
                            new NetworkUtilsImpl().executeJsonObjectRequest(Request.Method.GET, new StringBuilder(endpoint), urlParams, new NetworkRequestListener<JSONObject>() {
                                @Override
                                public void onSuccess(JSONObject jsonResponse) {
                                    Client client = new Gson().fromJson(jsonResponse.toString(), Client.class);
                                    intent.putExtra(com.woystech.fasdes.BundleKey.EXTRA_CLIENT,client);
                                    startActivity(intent);
                                    finish();
                                }

                                @Override
                                public void onError(VolleyError error) {
                                    Log.v(TAG, error.getMessage());
                                }
                            });
                        }
                        catch (Exception e){

                        }
                    }
                }
            }
        };
        timer.start();
    }

    // Method to check connection status
    private void checkConnection() {
        boolean isConnected = NetworkReceiver.isConnected();
        if (!isConnected) {
            Intent intent = new Intent(SplashActivity.this, NoNetworkActivity.class);
            startActivityForResult(intent, ApplicationConstants.REQUEST_CODE_NETWORK);

        }
    }

    private void initEndpointAndUrlParam(long id) {
        endpoint = Endpoint.CLIENT+id;
        urlParams = new HashMap<>();

    }
}
