package com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel;

import java.util.ArrayList;

/**
 * @author by nisie on 8/21/17.
 */

public class FilterPassModel {
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