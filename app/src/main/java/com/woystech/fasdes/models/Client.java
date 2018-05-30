package com.woystech.fasdes.models;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;

/**
 * Created by firok on 5/20/2017.
 */

public class Client extends User {

    @SerializedName("gender")
    private String gender;

    public Client(long id, String userName, String password, String phoneNumber, String email, Address address) {
        super(id, userName, password, phoneNumber, email, address);
    }


    public Client(String userName, String password, String phoneNumber, String email, Address address) {
        super(userName, password, phoneNumber, email, address);
    }

    public Client(String userName, String phoneNumber, String email, String gender) {
        super(userName, phoneNumber, email);
        this.gender = gender;
    }

    public Client(Parcel in) {
        super(in);
        gender = in.readString();
    }

    public static final Creator<Client> CREATOR = new Creator<Client>() {
        @Override
        public Client createFromParcel(Parcel in) {
            return new Client(in);
        }

        @Override
        public Client[] newArray(int size) {
            return new Client[size];
        }
    };

    @Override
    public int describeContents() {
        return super.describeContents();
    }

    @Override
    public void writeToParcel(Parcel out, int flag) {
        super.writeToParcel(out, flag);
        out.writeString(gender);
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
