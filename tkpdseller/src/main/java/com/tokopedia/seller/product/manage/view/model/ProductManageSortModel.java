package com.tokopedia.seller.product.manage.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.seller.base.view.adapter.ItemIdType;
import com.tokopedia.seller.product.manage.constant.SortProductOption;

/**
 * Created by zulfikarrahman on 9/26/17.
 */

public class ProductManageSortModel implements ItemIdType, Parcelable {

    public static final int TYPE = 223;

    @SortProductOption
    String sortId;

    String titleSort;

    @Override
    public int getType() {
        return TYPE;
    }

    @SortProductOption
    public String getId() {
        return sortId;
    }

    @Override
    @SortProductOption
    public String getItemId() {
        return sortId;
    }

    public void setSortId(@SortProductOption String sortId) {
        this.sortId = sortId;
    }

    public @SortProductOption String getTitleSort() {
        return titleSort;
    }

    public void setTitleSort(String titleSort) {
        this.titleSort = titleSort;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.sortId);
        dest.writeString(this.titleSort);
    }

    public ProductManageSortModel() {
    }

    protected ProductManageSortModel(Parcel in) {
        this.sortId = in.readString();
        this.titleSort = in.readString();
    }

    public static final Creator<ProductManageSortModel> CREATOR = new Creator<ProductManageSortModel>() {
        @Override
        public ProductManageSortModel createFromParcel(Parcel source) {
            return new ProductManageSortModel(source);
        }

        @Override
        public ProductManageSortModel[] newArray(int size) {
            return new ProductManageSortModel[size];
        }
    };
}
