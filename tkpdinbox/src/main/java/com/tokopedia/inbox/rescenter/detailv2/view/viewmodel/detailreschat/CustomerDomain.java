package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yoasfs on 10/10/17.
 */

public class CustomerDomain implements Parcelable {
    public static final Parcelable.Creator<CustomerDomain> CREATOR = new Parcelable.Creator<CustomerDomain>() {
        @Override
        public CustomerDomain createFromParcel(Parcel source) {
            return new CustomerDomain(source);
        }

        @Override
        public CustomerDomain[] newArray(int size) {
            return new CustomerDomain[size];
        }
    };
    private int id;
    private String name;
    private String url;
    private String imageThumb;
    private String image;

    public CustomerDomain(int id,
                          String name,
                          String url,
                          String imageThumb,
                          String image) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.imageThumb = imageThumb;
        this.image = image;
    }

    protected CustomerDomain(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.url = in.readString();
        this.imageThumb = in.readString();
        this.image = in.readString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageThumb() {
        return imageThumb;
    }

    public void setImageThumb(String imageThumb) {
        this.imageThumb = imageThumb;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.url);
        dest.writeString(this.imageThumb);
        dest.writeString(this.image);
    }
}
