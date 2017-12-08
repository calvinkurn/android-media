package com.tokopedia.discovery.newdiscovery.category.presentation.product.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by alifa on 5/23/17.
 */

public class BannerModel implements Parcelable{

    private Integer position;
    private String imageUrl;
    private String url;

    public BannerModel() {

    }

    protected BannerModel(Parcel in) {
        position = in.readByte() == 0x00 ? null : in.readInt();
        imageUrl = in.readString();
        url = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (position == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(position);
        }
        dest.writeString(imageUrl);
        dest.writeString(url);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<BannerModel> CREATOR = new Parcelable.Creator<BannerModel>() {
        @Override
        public BannerModel createFromParcel(Parcel in) {
            return new BannerModel(in);
        }

        @Override
        public BannerModel[] newArray(int size) {
            return new BannerModel[size];
        }
    };

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
