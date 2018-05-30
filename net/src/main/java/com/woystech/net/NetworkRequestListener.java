package com.woystech.net;

import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by firok on 7/23/2016.
 */
public interface NetworkRequestListener<T> {

    /**
     * Success callback
     *
     * @param jsonResponse
     */
    void onSuccess(T jsonResponse);

    /**
     * Error callback
     *
     * @param error
     */
    void onError(VolleyError error);
}
