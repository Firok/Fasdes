package com.woystech.fasdes.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.woystech.common.ApplicationConstants;
import com.woystech.common.Helper;
import com.woystech.fasdes.BundleKey;
import com.woystech.fasdes.R;
import com.woystech.fasdes.activities.NoNetworkActivity;
import com.woystech.fasdes.adapters.CategoryAdapter;
import com.woystech.fasdes.models.Category;
import com.woystech.fasdes.models.CategoryListener;
import com.woystech.fasdes.models.Client;
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
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by firok on 4/2/2017.
 */

public class CategoriesFragment extends Fragment {

    private static final String TAG = CategoriesFragment.class.getSimpleName();

    @BindView(R.id.rvCategories)
    RecyclerView rvCategories;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
    @BindView(R.id.follow)
    RadioButton follow;
    @BindView(R.id.unfollow)
    RadioButton unfollow;
    Unbinder unbinder;

    String endpoint;
    Map<String, String> urlParams;

    private List<Category> categoryList;
    private GridLayoutManager gridLayoutManager;
    private CategoryAdapter categoryAdapter;
    Client client;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, container, false);
        unbinder = ButterKnife.bind(this, view);
        checkConnection();
        client = getActivity().getIntent().getParcelableExtra(BundleKey.EXTRA_CLIENT);
        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        rvCategories.setLayoutManager(gridLayoutManager);
        //calling load categories method
        initFollowEndpointAndUrlParams(client.getId());
        loadCategories(1);

        return view;
    }

    @OnClick(R.id.follow)
    public void onClickFollowing() {
        initFollowEndpointAndUrlParams(client.getId());
        loadCategories(1);
    }

    @OnClick(R.id.unfollow)
    public void onClickUnFollowing() {
        initNotFllowEndpointAndUrlParams(client.getId());
        loadCategories(0);
    }

    // Method to check connection status
    private void checkConnection() {
        boolean isConnected = NetworkReceiver.isConnected();
        if (!isConnected) {
            Intent intent = new Intent(getContext(), NoNetworkActivity.class);
            startActivityForResult(intent, ApplicationConstants.REQUEST_CODE_NETWORK);

        }
    }

    private void loadCategories(final int status) {
        categoryList = new ArrayList<>();
        try {
            new NetworkUtilsImpl().executeJsonArrayRequest(Request.Method.GET, new StringBuilder(endpoint), urlParams, new NetworkRequestListener<JSONArray>() {
                @Override
                public void onSuccess(JSONArray jsonResponse) {
                    categoryList = new ArrayList(Arrays.asList(new Gson().fromJson(jsonResponse.toString(), Category[].class)));
                    Log.v(TAG, "" + categoryList.toString());
                    if (!Helper.isEmpty(categoryList)) {
                        categoryAdapter = new CategoryAdapter(getContext(), categoryList,status);
                        rvCategories.setAdapter(categoryAdapter);
                        categoryAdapter.notifyDataSetChanged();
                    }
                    else {
                        categoryAdapter = new CategoryAdapter(getContext(), categoryList,status);
                        rvCategories.setAdapter(categoryAdapter);
                        categoryAdapter.notifyDataSetChanged();
                    }
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

    private void initFollowEndpointAndUrlParams(long id) {
        endpoint = Endpoint.CLIENT + id + "/follows/categories";
        urlParams = new HashMap<>();
    }

    private void initNotFllowEndpointAndUrlParams(long id) {
        endpoint = Endpoint.CLIENT + id + "/notfollows/categories";
        urlParams = new HashMap<>();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
