package com.tokopedia.seller.opportunity.viewmodel;

import java.util.ArrayList;

/**
 * Created by nisie on 3/6/17.
 */

public class OpportunityListPageViewModel {
    OpportunityViewModel opportunityViewModel;
    ArrayList<ShippingTypeViewModel> listShippingType;
    ArrayList<SortingTypeViewModel> listSortingType;
    ArrayList<CategoryViewModel> listCategory;

    public OpportunityListPageViewModel() {

    }

    public OpportunityViewModel getOpportunityViewModel() {
        return opportunityViewModel;
    }

    public void setOpportunityViewModel(OpportunityViewModel opportunityViewModel) {
        this.opportunityViewModel = opportunityViewModel;
    }

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
}
