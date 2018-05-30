package com.woystech.fasdes.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lupitisa on 3/23/2017.
 */

public class Follow implements Parcelable {


    @SerializedName("client")
    private Client client;

    @SerializedName("category")
    private Category category;

    @SerializedName("status")
    private int status;

    public Follow(Client client, Category category) {
        this.client = client;
        this.category = category;
    }

    public Follow(Parcel in){
        client = in.readParcelable(Client.class.getClassLoader());
        category = in.readParcelable(Category.class.getClassLoader());
        status = in.readInt();
    }

    public static final Creator<Follow> CREATOR = new Creator<Follow>() {
        @Override
        public Follow createFromParcel(Parcel in) {
            return new Follow(in);
        }

        @Override
        public Follow[] newArray(int size) {
            return new Follow[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
            out.writeParcelable(client,i);
        out.writeParcelable(category,i);
        out.writeInt(status);
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
