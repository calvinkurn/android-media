package com.tokopedia.seller.product.edit.view.model.categoryrecomm;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Test on 5/12/2017.
 */

public class ProductCategoryIdViewModel implements Parcelable{
    private int id;
    private String name;

    public ProductCategoryIdViewModel(int id, String name) {
        this.id = id;
        this.name = name;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
    }

    protected ProductCategoryIdViewModel(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
    }

    public static final Creator<ProductCategoryIdViewModel> CREATOR = new Creator<ProductCategoryIdViewModel>() {
        @Override
        public ProductCategoryIdViewModel createFromParcel(Parcel source) {
            return new ProductCategoryIdViewModel(source);
        }

        @Override
        public ProductCategoryIdViewModel[] newArray(int size) {
            return new ProductCategoryIdViewModel[size];
        }
    };
}

