package com.tokopedia.seller.opportunity.viewmodel;

import com.tokopedia.seller.opportunity.viewmodel.opportunitylist.FilterPass;

import java.util.ArrayList;

/**
 * @author by nisie on 6/15/17.
 */

public class OpportunityFilterPassModel {
    private ArrayList<FilterViewModel> listFilter;
    private ArrayList<FilterPass> listPass;

    public ArrayList<FilterViewModel> getListFilter() {
        return listFilter;
    }

    public void setListFilter(ArrayList<FilterViewModel> listFilter) {
        this.listFilter = listFilter;
    }

    public ArrayList<FilterPass> getListPass() {
        return listPass;
    }

    public void setListPass(ArrayList<FilterPass> listPass) {
        this.listPass = listPass;
    }
}
