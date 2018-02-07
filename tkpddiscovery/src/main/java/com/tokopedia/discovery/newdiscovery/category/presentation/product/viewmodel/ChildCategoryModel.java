package com.tokopedia.discovery.newdiscovery.category.presentation.product.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by alifa on 3/24/17.
 */

public class ChildCategoryModel implements Parcelable{

    private String categoryId = "";
    private String categoryName = "";
    private String categoryImageUrl = "";
    private String categoryUrl = "";

    public ChildCategoryModel() {

    }

    protected ChildCategoryModel(Parcel in) {
        categoryId = in.readString();
        categoryName = in.readString();
        categoryImageUrl = in.readString();
        categoryUrl = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(categoryId);
        dest.writeString(categoryName);
        dest.writeString(categoryImageUrl);
        dest.writeString(categoryUrl);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ChildCategoryModel> CREATOR = new Parcelable.Creator<ChildCategoryModel>() {
        @Override
        public ChildCategoryModel createFromParcel(Parcel in) {
            return new ChildCategoryModel(in);
        }

        @Override
        public ChildCategoryModel[] newArray(int size) {
            return new ChildCategoryModel[size];
        }
    };

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryImageUrl() {
        return categoryImageUrl;
    }

    public void setCategoryImageUrl(String categoryImageUrl) {
        this.categoryImageUrl = categoryImageUrl;
    }

    public String getCategoryUrl() {
        return categoryUrl;
    }

    public void setCategoryUrl(String categoryUrl) {
        this.categoryUrl = categoryUrl;
    }

}