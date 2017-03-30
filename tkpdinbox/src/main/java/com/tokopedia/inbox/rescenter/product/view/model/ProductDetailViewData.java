package com.tokopedia.inbox.rescenter.product.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by hangnadi on 3/28/17.
 */

public class ProductDetailViewData implements Parcelable {

    private ArrayList<Attachment> attachment;
    private String productName;
    private String productPrice;
    private String productThumbUrl;
    private String trouble;
    private String troubleReason;

    public ProductDetailViewData() {
    }

    public ArrayList<Attachment> getAttachment() {
        return attachment;
    }

    public void setAttachment(ArrayList<Attachment> attachment) {
        this.attachment = attachment;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductThumbUrl() {
        return productThumbUrl;
    }

    public void setProductThumbUrl(String productThumbUrl) {
        this.productThumbUrl = productThumbUrl;
    }


    public String getTrouble() {
        return trouble;
    }

    public void setTrouble(String trouble) {
        this.trouble = trouble;
    }

    public String getTroubleReason() {
        return troubleReason;
    }

    public void setTroubleReason(String troubleReason) {
        this.troubleReason = troubleReason;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.attachment);
        dest.writeString(this.productName);
        dest.writeString(this.productPrice);
        dest.writeString(this.productThumbUrl);
        dest.writeString(this.trouble);
        dest.writeString(this.troubleReason);
    }

    protected ProductDetailViewData(Parcel in) {
        this.attachment = in.createTypedArrayList(Attachment.CREATOR);
        this.productName = in.readString();
        this.productPrice = in.readString();
        this.productThumbUrl = in.readString();
        this.trouble = in.readString();
        this.troubleReason = in.readString();
    }

    public static final Creator<ProductDetailViewData> CREATOR = new Creator<ProductDetailViewData>() {
        @Override
        public ProductDetailViewData createFromParcel(Parcel source) {
            return new ProductDetailViewData(source);
        }

        @Override
        public ProductDetailViewData[] newArray(int size) {
            return new ProductDetailViewData[size];
        }
    };
}
