package com.woystech.fasdes.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by firok on 5/20/2017.
 */

public class Address implements Parcelable{

    @SerializedName("fullAddress")
    private String fullAdress;
    @SerializedName("city")
    private String city;
    @SerializedName("country")
    private String country;
    @SerializedName("pincode")
    private String pincode;
    @SerializedName("street")
    private String street;
    @SerializedName("streetNumber")
    private int streetNumber;

    public Address(){

    }

    public Address(String fullAdress, String city, String country, String pincode, String street, int streetNumber) {
        this.fullAdress = fullAdress;
        this.city = city;
        this.country = country;
        this.pincode = pincode;
        this.street = street;
        this.streetNumber = streetNumber;
    }

    public Address(String fullAdress, String city, String country, String pincode, String street) {
        this.fullAdress = fullAdress;
        this.city = city;
        this.country = country;
        this.pincode = pincode;
        this.street = street;
    }

    public Address(Parcel in){
        city =in.readString();
        country = in.readString();
        pincode = in.readString();
        street = in.readString();
        streetNumber = in.readInt();
    }

    public static final Creator<Address> CREATOR = new Creator<Address>() {
        @Override
        public Address createFromParcel(Parcel in) {
            return new Address(in);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {

        out.writeString(city);
        out.writeString(country);
        out.writeString(pincode);
        out.writeString(street);
        out.writeInt(streetNumber);
    }

    public String getFullAdress() {
        return fullAdress;
    }

    public void setFullAdress(String fullAdress) {
        this.fullAdress = fullAdress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(int streetNumber) {
        this.streetNumber = streetNumber;
    }
}
