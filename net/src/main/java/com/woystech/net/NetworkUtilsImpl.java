package com.woystech.net;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.woystech.net.application.AppController;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by firok on 7/23/2016.
 */
public class NetworkUtilsImpl implements NetworkUtils {

    public static final int REQUEST_TIMEOUT = 5 * 1000;
    public static final int MAX_RETRIES = 3;
    public static final int BACKOFF_MULTIPLIER = 1;

    public void appendUrlParamsToEndpoint(StringBuilder endpoint, Map<String, String> urlParams){
        if (urlParams != null) {
            int i = 0;
            for (Map.Entry<String, String> entry : urlParams.entrySet()) {
                if (i == 0) {
                    endpoint.append("?");
                } else {
                    endpoint.append("&");
                }
                endpoint.append(entry.getKey()).append("=").append(entry.getValue());
                i++;
            }
        }
    }


    @Override
    public void executeJsonObjectRequest(int method, StringBuilder endpoint, Map<String, String> urlParams, final NetworkRequestListener<JSONObject> networkRequestListener) {

        appendUrlParamsToEndpoint(endpoint,urlParams);

        JsonObjectRequest jsonRequest = new JsonObjectRequest(method, endpoint.toString(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        networkRequestListener.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        networkRequestListener.onError(error);
                    }
                }
        );

        jsonRequest.setRetryPolicy(new CustomRetryPolicy(REQUEST_TIMEOUT, MAX_RETRIES , BACKOFF_MULTIPLIER));

        AppController.getInstance().addToRequestQueue(jsonRequest);
    }

    @Override
    public void executeJsonArrayRequest(int method, StringBuilder endpoint, Map<String, String> urlParams, final NetworkRequestListener<JSONArray> networkRequestListener) {

        appendUrlParamsToEndpoint(endpoint, urlParams);

        JsonArrayRequest jsonRequest = new JsonArrayRequest(method, endpoint.toString(), null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                networkRequestListener.onSuccess(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        networkRequestListener.onError(error);
                    }
                });

        jsonRequest.setRetryPolicy(new CustomRetryPolicy(REQUEST_TIMEOUT, MAX_RETRIES, BACKOFF_MULTIPLIER));

        AppController.getInstance().addToRequestQueue(jsonRequest);

    }

    @Override
    public void sendRequest(int method, StringBuilder endpoint, JSONObject data, final NetworkRequestListener<JSONObject> networkRequestListener) {

        JsonObjectRequest jsonRequest = new JsonObjectRequest(method, endpoint.toString(), data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        networkRequestListener.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        networkRequestListener.onError(error);
                    }
                }
        ){

        };

        jsonRequest.setRetryPolicy(new CustomRetryPolicy(REQUEST_TIMEOUT, MAX_RETRIES , BACKOFF_MULTIPLIER));

        AppController.getInstance().addToRequestQueue(jsonRequest);
    }
}
