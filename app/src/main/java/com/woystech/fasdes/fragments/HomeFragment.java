package com.woystech.fasdes.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.woystech.common.ApplicationConstants;
import com.woystech.common.VolleyCallback;
import com.woystech.fasdes.BundleKey;
import com.woystech.fasdes.R;
import com.woystech.fasdes.activities.ItemDetailsActivity;
import com.woystech.fasdes.activities.LoginActivity;
import com.woystech.fasdes.activities.MainActivity;
import com.woystech.fasdes.activities.NoNetworkActivity;
import com.woystech.fasdes.adapters.ItemAdapter;
import com.woystech.fasdes.config.Config;
import com.woystech.fasdes.models.Client;
import com.woystech.fasdes.models.Item;
import com.woystech.fasdes.net.Endpoint;
import com.woystech.net.NetworkRequestListener;
import com.woystech.net.NetworkUtilsImpl;
import com.woystech.net.application.AppController;
import com.woystech.net.connectivity.NetworkReceiver;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by firok on 4/2/2017.
 */

public class HomeFragment extends Fragment {

    private static final String TAG = CategoriesFragment.class.getSimpleName();

    @BindView(R.id.rvItems)
    RecyclerView rvItems;
    @BindView(R.id.unfollowing_message)
    TextView unfollowing_message;

    Unbinder unbinder;

    String endpoint;
    Map<String, String> urlParams;
    private long start;
    private long size;
    private boolean loading = false;

    private List<Item> itemList;
    private StaggeredGridLayoutManager gridLayoutManager;
    private ItemAdapter itemAdapter;
    Client client;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        checkConnection();
        client = getActivity().getIntent().getParcelableExtra(BundleKey.EXTRA_CLIENT);
        unfollowing_message.setText(client.getUserName() + " " + getActivity().getString(R.string.unfollowing_message));
        start = 1;
        size = Config.DEFAULT_SIZE;
        if (client!=null) {
            initEndpointAndUrlParams(client.getId(), start, size);
            itemList = new ArrayList<>();

            gridLayoutManager = new StaggeredGridLayoutManager(2, 1);
            rvItems.setLayoutManager(gridLayoutManager);
            rvItems.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    if (dy > 0) {
                        //check for scroll down
                        loadMoreItems();
                    }
                }
            });

            itemAdapter = new ItemAdapter(getContext(), itemList);
            rvItems.setAdapter(itemAdapter);
            loadItemList(new VolleyCallback<List<Item>>() {
                @Override
                public void onSuccess(List<Item> items) {
                    if (!items.isEmpty()) {
                        unfollowing_message.setVisibility(View.GONE);
                        itemList.clear();
                        itemList.addAll(items);
                        itemAdapter.notifyDataSetChanged();
                        loading = false;
                    } else {
                        unfollowing_message.setText(client.getUserName() + " " + getActivity().getString(R.string.unfollowing_message));
                        unfollowing_message.setVisibility(View.VISIBLE);
                    }
                }
            });

            if(itemList.isEmpty()){
                unfollowing_message.setVisibility(View.VISIBLE);
            }
        }
        return view;
    }

    /**
     * init endpoint and url params
     *
     * @param size
     * @param start
     */
    private void initEndpointAndUrlParams(long id, long start, long size) {
        endpoint = Endpoint.CLIENT + id + "/follows/items";
        urlParams = new HashMap<>();
        urlParams.put(Config.START, String.valueOf(start));
        urlParams.put(Config.SIZE, String.valueOf(size));

    }

    // Method to check connection status
    private void checkConnection() {
        boolean isConnected = NetworkReceiver.isConnected();
        if (!isConnected) {
            Intent intent = new Intent(getContext(), NoNetworkActivity.class);
            startActivityForResult(intent, ApplicationConstants.REQUEST_CODE_NETWORK);

        }
    }

    private void loadMoreItems() {
        int visibleItemCount = gridLayoutManager.getChildCount();
        int totalItemCount = gridLayoutManager.getItemCount();
        int firstVisibleItemsGrid[] = new int[2];
        int pastVisibleItems = gridLayoutManager.findFirstVisibleItemPositions(firstVisibleItemsGrid)[0];

        if (!loading) {
            if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                loading = true;
                Log.v(TAG, "Reached last Item!");

                //Fetching new data...

                start = size;
                size = size + Config.DEFAULT_SIZE;
                initEndpointAndUrlParams(client.getId(), start, size);


                loadItemList(new VolleyCallback<List<Item>>() {
                    @Override
                    public void onSuccess(List<Item> items) {
                        if (!items.isEmpty()) {
                            // itemList.clear();
                            itemList.addAll(items);
                            itemAdapter.notifyDataSetChanged();
                            loading = false;
                        }
                    }
                });

            }
        }

    }

    private void loadItemList(final VolleyCallback<List<Item>> callback) {
        new NetworkUtilsImpl().executeJsonArrayRequest(Request.Method.GET, new StringBuilder(endpoint), urlParams, new NetworkRequestListener<JSONArray>() {
            @Override
            public void onSuccess(JSONArray jsonResponse) {
                List<Item> items;
                items = Arrays.asList(new Gson().fromJson(jsonResponse.toString(), Item[].class));
                callback.onSuccess(items);
            }

            @Override
            public void onError(VolleyError error) {

            }
        })
        ;
    }


}
