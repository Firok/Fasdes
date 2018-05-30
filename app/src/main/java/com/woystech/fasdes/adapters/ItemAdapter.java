package com.woystech.fasdes.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.woystech.common.ItemClickListener;
import com.woystech.fasdes.BundleKey;
import com.woystech.fasdes.R;
import com.woystech.fasdes.activities.ItemDetailsActivity;
import com.woystech.fasdes.models.Item;
import com.woystech.fasdes.net.Endpoint;
import com.woystech.net.application.AppController;
import com.woystech.net.view.FadeInNetworkImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by firok on 4/3/2017.
 */

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private static final String TAG = ItemAdapter.class.getSimpleName();

    private Context context;
    private List<Item> itemList;

    private ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public ItemAdapter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =LayoutInflater.from(context).inflate(R.layout.layout_item_list,parent,false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        final Item item = itemList.get(position);

        holder.name.setText(item.getName());
        if(item.getImagePath()!=null){
            String imagePath= Endpoint.IMAGE +"items/"+ item.getImagePath();
            Log.v(TAG,imagePath);
            holder.photo.setImageUrl(imagePath, imageLoader);
            holder.photo.setErrorImageResId(R.drawable.loading);
        }

        holder.setOnItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(context, ItemDetailsActivity.class);
                intent.putExtra(BundleKey.EXTRA_ITEM,item);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ItemClickListener listener;

        @BindView(R.id.photo)
        protected FadeInNetworkImageView photo;

        @BindView(R.id.name)
        protected TextView name;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void setOnItemClickListener(ItemClickListener listener){
            this.listener = listener;
        }


        @Override
        public void onClick(View view) {
            listener.onClick(view,getPosition());
        }
    }
}
