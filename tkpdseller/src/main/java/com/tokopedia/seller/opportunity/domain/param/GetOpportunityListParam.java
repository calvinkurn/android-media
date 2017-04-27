package com.tokopedia.seller.opportunity.domain.param;

import com.tokopedia.seller.opportunity.viewmodel.opportunitylist.FilterPass;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by nisie on 3/2/17.
 */
public class GetOpportunityListParam {
    private String page;
    private String query;
    private String sort;
    private ArrayList<FilterPass> listFilter;

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public ArrayList<FilterPass> getListFilter() {
        return listFilter;
    }

    public void setListFilter(ArrayList<FilterPass> listFilter) {
        this.listFilter = listFilter;
    }
}
