package com.tokopedia.seller.opportunity.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nisie on 4/7/17.
 */

public class OpportunityFilterActivityViewModel implements Parcelable{
    private List<CategoryViewModel> listCategory;
    private List<ShippingTypeViewModel> listShipping;
    private ArrayList<FilterItemViewModel> listTitle;

    public OpportunityFilterActivityViewModel() {
    }

    protected OpportunityFilterActivityViewModel(Parcel in) {
        listCategory = in.createTypedArrayList(CategoryViewModel.CREATOR);
        listShipping = in.createTypedArrayList(ShippingTypeViewModel.CREATOR);
        listTitle = in.createTypedArrayList(FilterItemViewModel.CREATOR);
    }

    public static final Creator<OpportunityFilterActivityViewModel> CREATOR = new Creator<OpportunityFilterActivityViewModel>() {
        @Override
        public OpportunityFilterActivityViewModel createFromParcel(Parcel in) {
            return new OpportunityFilterActivityViewModel(in);
        }

        @Override
        public OpportunityFilterActivityViewModel[] newArray(int size) {
            return new OpportunityFilterActivityViewModel[size];
        }
    };

    public List<CategoryViewModel> getListCategory() {
        return listCategory;
    }

    public void setListCategory(List<CategoryViewModel> listCategory) {
        this.listCategory = listCategory;
    }

    public List<ShippingTypeViewModel> getListShipping() {
        return listShipping;
    }

    public void setListShipping(List<ShippingTypeViewModel> listShipping) {
        this.listShipping = listShipping;
    }

    public ArrayList<FilterItemViewModel> getListTitle() {
        return listTitle;
    }

    public void setListTitle(ArrayList<FilterItemViewModel> listTitle) {
        this.listTitle = listTitle;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(listCategory);
        dest.writeTypedList(listShipping);
        dest.writeTypedList(listTitle);
    }
}
