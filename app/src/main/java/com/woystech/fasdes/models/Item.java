package com.woystech.fasdes.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by firok on 4/3/2017.
 */

public class Item implements Parcelable {

    @SerializedName("id")
    private long id;
    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String description;
    @SerializedName("imagePath")
    private String imagePath;
    @SerializedName("no_of_image")
    private int no_of_image;
    @SerializedName("category")
    private Category category;

    public Item() {
    }

    public Item(Parcel in){
        id = in.readLong();
        name = in.readString();
        description = in.readString();
        imagePath = in.readString();
        no_of_image = in.readInt();
        category = in.readParcelable(Category.class.getClassLoader());

    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeLong(id);
        out.writeString(name);
        out.writeString(description);
        out.writeString(imagePath);
        out.writeInt(no_of_image);
        out.writeParcelable(category,i);

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNo_of_image() {
        return no_of_image;
    }

    public void setNo_of_image(int no_of_image) {
        this.no_of_image = no_of_image;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
