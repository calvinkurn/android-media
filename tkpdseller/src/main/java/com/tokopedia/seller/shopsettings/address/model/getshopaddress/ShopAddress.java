package com.tokopedia.seller.shopsettings.address.model.getshopaddress;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.district_recommendation.domain.model.Token;

public class ShopAddress implements Parcelable {

    @SerializedName("list")
    @Expose
    private java.util.List<List> list = new ArrayList<>();

    @SerializedName("is_allow")
    @Expose
    private Integer isAllow;

    @SerializedName("token")
    @Expose
    private Token token;

    /**
     * 
     * @return
     *     The list
     */
    public java.util.List<List> getList() {
        return list;
    }

    /**
     * 
     * @param list
     *     The list
     */
    public void setList(java.util.List<List> list) {
        this.list = list;
    }

    /**
     * 
     * @return
     *     The isAllow
     */
    public Integer getIsAllow() {
        return isAllow;
    }

    /**
     * 
     * @param isAllow
     *     The is_allow
     */
    public void setIsAllow(Integer isAllow) {
        this.isAllow = isAllow;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.list);
        dest.writeValue(this.isAllow);
        dest.writeParcelable(this.token, flags);
    }

    public ShopAddress() {
    }

    protected ShopAddress(Parcel in) {
        this.list = new ArrayList<>();
        in.readList(this.list, List.class.getClassLoader());
        this.isAllow = (Integer) in.readValue(Integer.class.getClassLoader());
        this.token = in.readParcelable(Token.class.getClassLoader());
    }

    public static final Creator<ShopAddress> CREATOR = new Creator<ShopAddress>() {
        @Override
        public ShopAddress createFromParcel(Parcel source) {
            return new ShopAddress(source);
        }

        @Override
        public ShopAddress[] newArray(int size) {
            return new ShopAddress[size];
        }
    };
}
