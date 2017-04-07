package com.tokopedia.seller.opportunity.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by nisie on 3/6/17.
 */

public class CategoryViewModel implements Parcelable{
    int categoryId;
    String categoryName;
    int parent;
    int isHidden;
    int treeLevel;
    String identifier;
    List<CategoryViewModel> listChild;

    public CategoryViewModel() {
    }

    protected CategoryViewModel(Parcel in) {
        categoryId = in.readInt();
        categoryName = in.readString();
        parent = in.readInt();
        isHidden = in.readInt();
        treeLevel = in.readInt();
        identifier = in.readString();
        listChild = in.createTypedArrayList(CategoryViewModel.CREATOR);
    }

    public static final Creator<CategoryViewModel> CREATOR = new Creator<CategoryViewModel>() {
        @Override
        public CategoryViewModel createFromParcel(Parcel in) {
            return new CategoryViewModel(in);
        }

        @Override
        public CategoryViewModel[] newArray(int size) {
            return new CategoryViewModel[size];
        }
    };

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getTreeLevel() {
        return treeLevel;
    }

    public void setTreeLevel(int treeLevel) {
        this.treeLevel = treeLevel;
    }

    public List<CategoryViewModel> getListChild() {
        return listChild;
    }

    public void setListChild(List<CategoryViewModel> listChild) {
        this.listChild = listChild;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public int getIsHidden() {
        return isHidden;
    }

    public void setIsHidden(int isHidden) {
        this.isHidden = isHidden;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(categoryId);
        dest.writeString(categoryName);
        dest.writeInt(parent);
        dest.writeInt(isHidden);
        dest.writeInt(treeLevel);
        dest.writeString(identifier);
        dest.writeTypedList(listChild);
    }
}
