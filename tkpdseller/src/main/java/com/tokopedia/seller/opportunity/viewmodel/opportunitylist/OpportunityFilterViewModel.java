package com.tokopedia.seller.opportunity.viewmodel.opportunitylist;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.seller.opportunity.viewmodel.CategoryViewModel;
import com.tokopedia.seller.opportunity.viewmodel.ShippingTypeViewModel;
import com.tokopedia.seller.opportunity.viewmodel.SortingTypeViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nisie on 3/6/17.
 */

public class OpportunityFilterViewModel implements Parcelable{
    ArrayList<ShippingTypeViewModel> listShippingType;
    ArrayList<SortingTypeViewModel> listSortingType;
    ArrayList<CategoryViewModel> listCategory;

    public OpportunityFilterViewModel() {

    }

    protected OpportunityFilterViewModel(Parcel in) {
        listShippingType = in.createTypedArrayList(ShippingTypeViewModel.CREATOR);
        listSortingType = in.createTypedArrayList(SortingTypeViewModel.CREATOR);
        listCategory = in.createTypedArrayList(CategoryViewModel.CREATOR);
    }

    public static final Creator<OpportunityFilterViewModel> CREATOR = new Creator<OpportunityFilterViewModel>() {
        @Override
        public OpportunityFilterViewModel createFromParcel(Parcel in) {
            return new OpportunityFilterViewModel(in);
        }

        @Override
        public OpportunityFilterViewModel[] newArray(int size) {
            return new OpportunityFilterViewModel[size];
        }
    };

    public ArrayList<ShippingTypeViewModel> getListShippingType() {
        return listShippingType;
    }

    public void setListShippingType(ArrayList<ShippingTypeViewModel> listShippingType) {
        this.listShippingType = listShippingType;
    }

    public ArrayList<SortingTypeViewModel> getListSortingType() {
        return listSortingType;
    }

    public void setListSortingType(ArrayList<SortingTypeViewModel> listSortingType) {
        this.listSortingType = listSortingType;
    }

    public ArrayList<CategoryViewModel> getListCategory() {
        return listCategory;
    }

    public void setListCategory(ArrayList<CategoryViewModel> listCategory) {
        this.listCategory = listCategory;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(listShippingType);
        dest.writeTypedList(listSortingType);
        dest.writeTypedList(listCategory);
    }
}
