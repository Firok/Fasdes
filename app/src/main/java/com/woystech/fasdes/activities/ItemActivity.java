package com.woystech.fasdes.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.woystech.common.ApplicationConstants;
import com.woystech.common.VolleyCallback;
import com.woystech.fasdes.BundleKey;
import com.woystech.fasdes.R;
import com.woystech.fasdes.adapters.ItemAdapter;
import com.woystech.fasdes.models.Category;
import com.woystech.fasdes.models.Item;
import com.woystech.fasdes.net.Endpoint;
import com.woystech.net.NetworkRequestListener;
import com.woystech.net.NetworkUtilsImpl;
import com.woystech.net.connectivity.NetworkReceiver;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by firok on 4/3/2017.
 */

public class ItemActivity extends AppCompatActivity {

    private static final String TAG = ItemActivity.class.getSimpleName();

    @BindView(R.id.rvItems)
    RecyclerView rvItems;

    String endpoint;
    Map<String, String> urlParams;

    private VolleyCallback<List<Item>> hotelVolleyCallback;
    private List<Item> itemList;
    private StaggeredGridLayoutManager layoutManager;
    private ItemAdapter itemAdapter;
    private Category category;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        ButterKnife.bind(this);
        checkConnection();
        //get category from intent
        category = getCategoryFromIntent();

        if (category != null) {
            loadItems(new VolleyCallback<List<Item>>() {
                @Override
                public void onSuccess(List<Item> object) {
                    itemList = object;
                    itemAdapter = new ItemAdapter(ItemActivity.this, itemList);
                    layoutManager = new StaggeredGridLayoutManager(2, 1);
                    rvItems.setLayoutManager(layoutManager);
                    rvItems.setAdapter(itemAdapter);
                }
            });
        }
    }

    // Method to check connection status
    private void checkConnection() {
        boolean isConnected = NetworkReceiver.isConnected();
        if (!isConnected) {
            Intent intent = new Intent(ItemActivity.this, NoNetworkActivity.class);
            startActivityForResult(intent, ApplicationConstants.REQUEST_CODE_NETWORK);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void loadItems(final VolleyCallback<List<Item>> callback) {
        initEndpointAndUrlParams(category.getId());
        try {
            new NetworkUtilsImpl().executeJsonArrayRequest(Request.Method.GET, new StringBuilder(endpoint), urlParams, new NetworkRequestListener<JSONArray>() {
                @Override
                public void onSuccess(JSONArray jsonResponse) {
                    List<Item> itemList = new ArrayList<>();
                    itemList = Arrays.asList(new Gson().fromJson(jsonResponse.toString(), Item[].class));
                    callback.onSuccess(itemList);
                }

                @Override
                public void onError(VolleyError error) {
                    Log.v(TAG, error.getMessage());
                }
            });
        } catch (Exception e) {
            Log.v(TAG, e.getMessage());
        }

    }

    private void initEndpointAndUrlParams(long id) {
        endpoint = Endpoint.CATEGORY + id + "/items/";
        urlParams = new HashMap<>();
    }

    private Category getCategoryFromIntent() {
        Category category = null;
        if (getIntent().hasExtra(BundleKey.EXTRA_CATEGORY)) {
            category = getIntent().getParcelableExtra(BundleKey.EXTRA_CATEGORY);
        }
        return category;
    }
}
