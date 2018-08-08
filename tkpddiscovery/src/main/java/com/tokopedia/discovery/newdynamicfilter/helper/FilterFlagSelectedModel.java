package com.tokopedia.discovery.newdynamicfilter.helper;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

/**
 * Created by hangnadi on 10/18/17.
 */

public class FilterFlagSelectedModel implements Parcelable {
    private HashMap<String, Boolean> savedCheckedState;
    private HashMap<String, String> savedTextInput;
    private String categoryId;
    private String selectedCategoryRootId;
    private String selectedCategoryName;

    public void setSavedCheckedState(HashMap<String, Boolean> savedCheckedState) {
        this.savedCheckedState = savedCheckedState;
    }

    public HashMap<String, Boolean> getSavedCheckedState() {
        return savedCheckedState;
    }

    public void setSavedTextInput(HashMap<String, String> savedTextInput) {
        this.savedTextInput = savedTextInput;
    }

    public HashMap<String, String> getSavedTextInput() {
        return savedTextInput;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setSelectedCategoryRootId(String selectedCategoryRootId) {
        this.selectedCategoryRootId = selectedCategoryRootId;
    }

    public String getSelectedCategoryRootId() {
        return selectedCategoryRootId;
    }

    public void setSelectedCategoryName(String selectedCategoryName) {
        this.selectedCategoryName = selectedCategoryName;
    }

    public String getSelectedCategoryName() {
        return selectedCategoryName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this.savedCheckedState);
        dest.writeSerializable(this.savedTextInput);
        dest.writeString(this.categoryId);
        dest.writeString(this.selectedCategoryRootId);
        dest.writeString(this.selectedCategoryName);
    }

    public FilterFlagSelectedModel() {
    }

    @SuppressWarnings("unchecked")
    protected FilterFlagSelectedModel(Parcel in) {
        this.savedCheckedState = (HashMap<String, Boolean>) in.readSerializable();
        this.savedTextInput = (HashMap<String, String>) in.readSerializable();
        this.categoryId = in.readString();
        this.selectedCategoryRootId = in.readString();
        this.selectedCategoryName = in.readString();
    }

    public static final Parcelable.Creator<FilterFlagSelectedModel> CREATOR = new Parcelable.Creator<FilterFlagSelectedModel>() {
        @Override
        public FilterFlagSelectedModel createFromParcel(Parcel source) {
            return new FilterFlagSelectedModel(source);
        }

        @Override
        public FilterFlagSelectedModel[] newArray(int size) {
            return new FilterFlagSelectedModel[size];
        }
    };
}
