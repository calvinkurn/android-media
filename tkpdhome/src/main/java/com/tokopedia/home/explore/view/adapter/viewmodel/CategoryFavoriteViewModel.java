package com.tokopedia.home.explore.view.adapter.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.home.explore.domain.model.LayoutRows;
import com.tokopedia.home.explore.view.adapter.TypeFactory;

import java.util.List;

/**
 * Created by errysuprayogi on 1/31/18.
 */

public class CategoryFavoriteViewModel implements Visitable<TypeFactory>, Parcelable {

    private int sectionId;
    private String title;
    private List<LayoutRows> itemList;

    public CategoryFavoriteViewModel() {
    }

    protected CategoryFavoriteViewModel(Parcel in) {
        sectionId = in.readInt();
        title = in.readString();
        itemList = in.createTypedArrayList(LayoutRows.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(sectionId);
        dest.writeString(title);
        dest.writeTypedList(itemList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CategoryFavoriteViewModel> CREATOR = new Creator<CategoryFavoriteViewModel>() {
        @Override
        public CategoryFavoriteViewModel createFromParcel(Parcel in) {
            return new CategoryFavoriteViewModel(in);
        }

        @Override
        public CategoryFavoriteViewModel[] newArray(int size) {
            return new CategoryFavoriteViewModel[size];
        }
    };

    public int getSectionId() {
        return sectionId;
    }

    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<LayoutRows> getItemList() {
        return itemList;
    }

    public void setItemList(List<LayoutRows> itemList) {
        this.itemList = itemList;
    }

    @Override
    public int type(TypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
