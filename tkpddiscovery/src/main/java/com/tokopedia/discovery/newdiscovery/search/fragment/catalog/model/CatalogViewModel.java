package com.tokopedia.discovery.newdiscovery.search.fragment.catalog.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.discovery.newdiscovery.search.fragment.catalog.adapter.factory.CatalogAdapterTypeFactory;

/**
 * Created by hangnadi on 10/12/17.
 */

public class CatalogViewModel implements Visitable<CatalogAdapterTypeFactory>, Parcelable {

    private String ID;
    private String name;
    private String desc;
    private String image;
    private String image300;
    private String price;
    private String productCounter;
    private String URL;

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getID() {
        return ID;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage300(String image300) {
        this.image300 = image300;
    }

    public String getImage300() {
        return image300;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice() {
        return price;
    }

    public void setProductCounter(String productCounter) {
        this.productCounter = productCounter;
    }

    public String getProductCounter() {
        return productCounter;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getURL() {
        return URL;
    }

    @Override
    public int type(CatalogAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.ID);
        dest.writeString(this.name);
        dest.writeString(this.desc);
        dest.writeString(this.image);
        dest.writeString(this.image300);
        dest.writeString(this.price);
        dest.writeString(this.productCounter);
        dest.writeString(this.URL);
    }

    public CatalogViewModel() {
    }

    protected CatalogViewModel(Parcel in) {
        this.ID = in.readString();
        this.name = in.readString();
        this.desc = in.readString();
        this.image = in.readString();
        this.image300 = in.readString();
        this.price = in.readString();
        this.productCounter = in.readString();
        this.URL = in.readString();
    }

    public static final Parcelable.Creator<CatalogViewModel> CREATOR = new Parcelable.Creator<CatalogViewModel>() {
        @Override
        public CatalogViewModel createFromParcel(Parcel source) {
            return new CatalogViewModel(source);
        }

        @Override
        public CatalogViewModel[] newArray(int size) {
            return new CatalogViewModel[size];
        }
    };
}
