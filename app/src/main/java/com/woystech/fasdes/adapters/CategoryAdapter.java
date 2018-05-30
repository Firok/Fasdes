package com.woystech.fasdes.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.google.gson.Gson;
import com.woystech.common.ItemClickListener;
import com.woystech.fasdes.BundleKey;
import com.woystech.fasdes.R;
import com.woystech.fasdes.activities.ItemActivity;
import com.woystech.fasdes.activities.MainActivity;
import com.woystech.fasdes.activities.SignUpSecondStepActivity;
import com.woystech.fasdes.models.Category;
import com.woystech.fasdes.models.CategoryListener;
import com.woystech.fasdes.models.Client;
import com.woystech.fasdes.models.Follow;
import com.woystech.fasdes.net.Endpoint;
import com.woystech.net.NetworkRequestListener;
import com.woystech.net.NetworkUtilsImpl;
import com.woystech.net.application.AppController;
import com.woystech.net.view.FadeInNetworkImageView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lupitisa on 3/23/2017.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder> {

    private static final String TAG = CategoryAdapter.class.getSimpleName();

    private Context context;
    private List<Category> categoryList;
    private int status;
    private ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private String endpoint;
    private Map<String, String> urlParams;

    public CategoryAdapter(Context context, List<Category> categoryList, int status) {
        this.context = context;
        this.categoryList = categoryList;
        this.status = status;
    }

    @Override
    public CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_category_list_item, parent, false);

        return new CategoryHolder(view);
    }
    @Override
    public void onBindViewHolder(CategoryHolder holder, final int position) {
        final Category category = categoryList.get(position);

        holder.name.setText(category.getName());
        if(category.getImagePath()!=null){
            String imagePath= Endpoint.IMAGE + category.getImagePath();
            Log.v(TAG,imagePath);
            holder.photo.setImageUrl(imagePath, imageLoader);
            holder.photo.setErrorImageResId(R.drawable.loading);
        }

        if (status==1){
            holder.foolow.setText(R.string.unfollow);
        }
        else if (status==0){
            holder.foolow.setText(R.string.follow);
        }

        holder.foolow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) context;
                Client client = activity.getIntent().getParcelableExtra(BundleKey.EXTRA_CLIENT);
                Follow follow = new Follow(client,category);
                if (status==0){
                    initSaveFollowEndpointAndUrlParam();
                    try {
                        follow.setStatus(0);
                        JSONObject params = new JSONObject(new Gson().toJson(follow));
                        Log.v(TAG, params.toString());
                        new NetworkUtilsImpl().sendRequest(Request.Method.POST, new StringBuilder(endpoint), params, new NetworkRequestListener<JSONObject>() {
                            @Override
                            public void onSuccess(JSONObject jsonResponse) {
                                Log.v(TAG, "Merci");
                                Follow follow = new Gson().fromJson(jsonResponse.toString(), Follow.class);
                                Log.v(TAG, follow.toString());
                                categoryList.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position,categoryList.size());
                            }

                            @Override
                            public void onError(VolleyError error) {
                                Log.v(TAG, error + "Merci");
                            }
                        });
                    } catch (Exception e) {
                        Log.v(TAG, e.getMessage());
                    }
                }
                else if (status==1){
                    initUpdateFollowEndpointAndUrlParam(client.getId(),category.getId());
                    try {
                        JSONObject params = new JSONObject(new Gson().toJson(follow));
                        Log.v(TAG, params.toString());
                        new NetworkUtilsImpl().sendRequest(Request.Method.PUT, new StringBuilder(endpoint), params, new NetworkRequestListener<JSONObject>() {
                            @Override
                            public void onSuccess(JSONObject jsonResponse) {
                                Follow follow = new Gson().fromJson(jsonResponse.toString(), Follow.class);
                                Log.v(TAG, follow.toString());
                                categoryList.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position,categoryList.size());
                            }

                            @Override
                            public void onError(VolleyError error) {
                                Log.v(TAG, error + "Merci");
                            }
                        });
                    } catch (Exception e) {
                        Log.v(TAG, e.getMessage());
                    }
                }

            }

        });

        holder.setOnItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(context, ItemActivity.class);
                intent.putExtra(BundleKey.EXTRA_CATEGORY, category);
                context.startActivity(intent);
            }
        });

    }

    private void initSaveFollowEndpointAndUrlParam() {
        endpoint = Endpoint.FOLLOW;

    }

    private void initUpdateFollowEndpointAndUrlParam(long clientId, long categoryId) {
        endpoint = Endpoint.FOLLOW+clientId+"/"+categoryId;

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    static class CategoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.photo)
        protected FadeInNetworkImageView photo;

        @BindView(R.id.name)
        protected TextView name;

        @BindView(R.id.follow)
        protected Button foolow;

        ItemClickListener listener;

        public CategoryHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        public void setOnItemClickListener(ItemClickListener listener){
            this.listener = listener;
        }

        @Override
        public void onClick(View view) {
            this.listener.onClick(view, getPosition());
        }
    }
}
