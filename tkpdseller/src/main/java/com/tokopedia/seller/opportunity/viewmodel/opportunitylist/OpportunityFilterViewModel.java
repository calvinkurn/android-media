package com.tokopedia.seller.opportunity.viewmodel.opportunitylist;

import com.tokopedia.seller.opportunity.viewmodel.CategoryViewModel;
import com.tokopedia.seller.opportunity.viewmodel.ShippingTypeViewModel;
import com.tokopedia.seller.opportunity.viewmodel.SortingTypeViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nisie on 3/6/17.
 */

public class OpportunityFilterViewModel {
    List<ShippingTypeViewModel> listShippingType;
    List<SortingTypeViewModel> listSortingType;
    List<CategoryViewModel> listCategory;

    public OpportunityFilterViewModel() {

    }

    public List<ShippingTypeViewModel> getListShippingType() {
        return listShippingType;
    }

    public void setListShippingType(List<ShippingTypeViewModel> listShippingType) {
        this.listShippingType = listShippingType;
    }

    public List<SortingTypeViewModel> getListSortingType() {
        return listSortingType;
    }

    public void setListSortingType(List<SortingTypeViewModel> listSortingType) {
        this.listSortingType = listSortingType;
    }

    public List<CategoryViewModel> getListCategory() {
        return listCategory;
    }

    public void setListCategory(List<CategoryViewModel> listCategory) {
        this.listCategory = listCategory;
    }
}
