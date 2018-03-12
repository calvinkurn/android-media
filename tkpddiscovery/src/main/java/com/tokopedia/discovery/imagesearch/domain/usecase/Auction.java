
package com.tokopedia.discovery.imagesearch.domain.usecase;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Auction implements Serializable, Parcelable {

    @SerializedName("CustContent")
    @Expose
    private String custContent;
    @SerializedName("ItemId")
    @Expose
    private String itemId;
    @SerializedName("PicName")
    @Expose
    private String picName;
    @SerializedName("CatId")
    @Expose
    private String catId;
    @SerializedName("SortExprValues")
    @Expose
    private String sortExprValues;
    public final static Creator<Auction> CREATOR = new Creator<Auction>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Auction createFromParcel(Parcel in) {
            return new Auction(in);
        }

        public Auction[] newArray(int size) {
            return (new Auction[size]);
        }

    };
    private final static long serialVersionUID = -3142496252390364890L;

    protected Auction(Parcel in) {
        this.custContent = ((String) in.readValue((String.class.getClassLoader())));
        this.itemId = ((String) in.readValue((String.class.getClassLoader())));
        this.picName = ((String) in.readValue((String.class.getClassLoader())));
        this.catId = ((String) in.readValue((String.class.getClassLoader())));
        this.sortExprValues = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     */
    public Auction() {
    }

    /**
     * @param sortExprValues
     * @param catId
     * @param picName
     * @param itemId
     * @param custContent
     */
    public Auction(String custContent, String itemId, String picName, String catId, String sortExprValues) {
        super();
        this.custContent = custContent;
        this.itemId = itemId;
        this.picName = picName;
        this.catId = catId;
        this.sortExprValues = sortExprValues;
    }

    public String getCustContent() {
        return custContent;
    }

    public void setCustContent(String custContent) {
        this.custContent = custContent;
    }

    public Auction withCustContent(String custContent) {
        this.custContent = custContent;
        return this;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public Auction withItemId(String itemId) {
        this.itemId = itemId;
        return this;
    }

    public String getPicName() {
        return picName;
    }

    public void setPicName(String picName) {
        this.picName = picName;
    }

    public Auction withPicName(String picName) {
        this.picName = picName;
        return this;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public Auction withCatId(String catId) {
        this.catId = catId;
        return this;
    }

    public String getSortExprValues() {
        return sortExprValues;
    }

    public void setSortExprValues(String sortExprValues) {
        this.sortExprValues = sortExprValues;
    }

    public Auction withSortExprValues(String sortExprValues) {
        this.sortExprValues = sortExprValues;
        return this;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(custContent);
        dest.writeValue(itemId);
        dest.writeValue(picName);
        dest.writeValue(catId);
        dest.writeValue(sortExprValues);
    }

    public int describeContents() {
        return 0;
    }

}
