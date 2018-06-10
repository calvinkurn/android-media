
package com.tokopedia.seller.product.edit.view.model.edit;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductVideoViewModel implements Parcelable{

    //currently only from youtube
    public static final String YOUTUBE_SOURCE = "youtube";

    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName(value="type", alternate={"source"})
    @Expose
    private String source;

    public ProductVideoViewModel(String url) {
        this.url = url;
        this.source = YOUTUBE_SOURCE;
    }

    public ProductVideoViewModel(String url, String source) {
        this.url = url;
        this.source = source;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeString(this.source);
    }

    protected ProductVideoViewModel(Parcel in) {
        this.url = in.readString();
        this.source = in.readString();
    }

    public static final Creator<ProductVideoViewModel> CREATOR = new Creator<ProductVideoViewModel>() {
        @Override
        public ProductVideoViewModel createFromParcel(Parcel source) {
            return new ProductVideoViewModel(source);
        }

        @Override
        public ProductVideoViewModel[] newArray(int size) {
            return new ProductVideoViewModel[size];
        }
    };
}
