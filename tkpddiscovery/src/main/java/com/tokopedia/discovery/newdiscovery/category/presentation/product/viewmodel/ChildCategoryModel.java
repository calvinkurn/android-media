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
    private int isAdult = 0;

    public ChildCategoryModel() {

    }

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

    public int getIsAdult() {
        return isAdult;
    }

    public void setIsAdult(int isAdult) {
        this.isAdult = isAdult;
    }

    public String getCategoryUrl() {
        return categoryUrl;
    }

    public void setCategoryUrl(String categoryUrl) {
        this.categoryUrl = categoryUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.categoryId);
        dest.writeString(this.categoryName);
        dest.writeString(this.categoryImageUrl);
        dest.writeString(this.categoryUrl);
        dest.writeInt(this.isAdult);
    }

    protected ChildCategoryModel(Parcel in) {
        this.categoryId = in.readString();
        this.categoryName = in.readString();
        this.categoryImageUrl = in.readString();
        this.categoryUrl = in.readString();
        this.isAdult = in.readInt();
    }

    public static final Creator<ChildCategoryModel> CREATOR = new Creator<ChildCategoryModel>() {
        @Override
        public ChildCategoryModel createFromParcel(Parcel source) {
            return new ChildCategoryModel(source);
        }

        @Override
        public ChildCategoryModel[] newArray(int size) {
            return new ChildCategoryModel[size];
        }
    };
}