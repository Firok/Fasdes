package com.woystech.net;

import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by firok on 7/23/2016.
 */
public interface NetworkUtils {

    /**
     * Creates JsonRequest
     *
     * @param method
     * @param endpoint
     * @param urlParams
     * @param networkRequestListener
     */
    void executeJsonObjectRequest(int method, StringBuilder endpoint, Map<String,String> urlParams, NetworkRequestListener<JSONObject> networkRequestListener);

    /**
     *
     * @param method
     * @param endpoint
     * @param urlParams
     * @param networkRequestListener
     */
    void executeJsonArrayRequest(int method, StringBuilder endpoint, Map<String, String> urlParams, NetworkRequestListener<JSONArray> networkRequestListener);

    /**
     * Send Json Request
     * @param method
     * @param endpoint
     * @param data
     * @param networkRequestListener
     */
    void sendRequest(int method, StringBuilder endpoint, JSONObject data, NetworkRequestListener<JSONObject> networkRequestListener);
}
