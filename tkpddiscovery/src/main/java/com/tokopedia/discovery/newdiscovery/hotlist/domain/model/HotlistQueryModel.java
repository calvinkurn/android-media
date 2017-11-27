package com.tokopedia.discovery.newdiscovery.hotlist.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hangnadi on 10/8/17.
 */

public class HotlistQueryModel implements Parcelable {

    private String orderBy;
    private String queryKey;
    private String hotlistID;
    private String shopID;
    private String filterGoldMerchant;
    private String priceMax;
    private String priceMin;
    private String categoryID;
    private String negativeKeyword;

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setQueryKey(String queryKey) {
        this.queryKey = queryKey;
    }

    public String getQueryKey() {
        return queryKey;
    }

    public void setHotlistID(String hotlistID) {
        this.hotlistID = hotlistID;
    }

    public String getHotlistID() {
        return hotlistID;
    }

    public void setShopID(String shopID) {
        this.shopID = shopID;
    }

    public String getShopID() {
        return shopID;
    }

    public void setFilterGoldMerchant(String filterGoldMerchant) {
        this.filterGoldMerchant = filterGoldMerchant;
    }

    public String getFilterGoldMerchant() {
        return filterGoldMerchant;
    }

    public void setPriceMax(String priceMax) {
        this.priceMax = priceMax;
    }

    public String getPriceMax() {
        if (priceMax == null || priceMax.isEmpty() || priceMax.equals("0")) {
            return "";
        }
        return priceMax;
    }

    public void setPriceMin(String priceMin) {
        this.priceMin = priceMin;
    }

    public String getPriceMin() {
        if (priceMin == null || priceMin.isEmpty() || priceMin.equals("0")) {
            return "";
        }
        return priceMin;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setNegativeKeyword(String negativeKeyword) {
        this.negativeKeyword = negativeKeyword;
    }

    public String getNegativeKeyword() {
        return negativeKeyword;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.orderBy);
        dest.writeString(this.queryKey);
        dest.writeString(this.hotlistID);
        dest.writeString(this.shopID);
        dest.writeString(this.filterGoldMerchant);
        dest.writeString(this.priceMax);
        dest.writeString(this.priceMin);
        dest.writeString(this.categoryID);
        dest.writeString(this.negativeKeyword);
    }

    public HotlistQueryModel() {
    }

    protected HotlistQueryModel(Parcel in) {
        this.orderBy = in.readString();
        this.queryKey = in.readString();
        this.hotlistID = in.readString();
        this.shopID = in.readString();
        this.filterGoldMerchant = in.readString();
        this.priceMax = in.readString();
        this.priceMin = in.readString();
        this.categoryID = in.readString();
        this.negativeKeyword = in.readString();
    }

    public static final Parcelable.Creator<HotlistQueryModel> CREATOR = new Parcelable.Creator<HotlistQueryModel>() {
        @Override
        public HotlistQueryModel createFromParcel(Parcel source) {
            return new HotlistQueryModel(source);
        }

        @Override
        public HotlistQueryModel[] newArray(int size) {
            return new HotlistQueryModel[size];
        }
    };
}
