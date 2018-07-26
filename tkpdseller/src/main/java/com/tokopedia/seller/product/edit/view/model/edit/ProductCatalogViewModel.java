
package com.tokopedia.seller.product.edit.view.model.edit;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductCatalogViewModel implements Parcelable {

    public static final int ACTIVE_STATUS = 1;
    @SerializedName("catalog_id")
    @Expose
    private long catalogId;
    @SerializedName("catalog_name")
    @Expose
    private String catalogName;
    @SerializedName("catalog_url")
    @Expose
    private String catalogUrl;
    @SerializedName("catalog_status")
    @Expose
    private long catalogStatus = ACTIVE_STATUS;

    public long getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(long catalogId) {
        this.catalogId = catalogId;
    }

    public String getCatalogName() {
        return catalogName;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }

    public String getCatalogUrl() {
        return catalogUrl;
    }

    public void setCatalogUrl(String catalogUrl) {
        this.catalogUrl = catalogUrl;
    }

    public long getCatalogStatus() {
        return catalogStatus;
    }

    public void setCatalogStatus(long catalogStatus) {
        this.catalogStatus = catalogStatus;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.catalogId);
        dest.writeString(this.catalogName);
        dest.writeString(this.catalogUrl);
        dest.writeLong(this.catalogStatus);
    }

    public ProductCatalogViewModel() {
    }

    protected ProductCatalogViewModel(Parcel in) {
        this.catalogId = in.readLong();
        this.catalogName = in.readString();
        this.catalogUrl = in.readString();
        this.catalogStatus = in.readLong();
    }

    public static final Creator<ProductCatalogViewModel> CREATOR = new Creator<ProductCatalogViewModel>() {
        @Override
        public ProductCatalogViewModel createFromParcel(Parcel source) {
            return new ProductCatalogViewModel(source);
        }

        @Override
        public ProductCatalogViewModel[] newArray(int size) {
            return new ProductCatalogViewModel[size];
        }
    };
}
