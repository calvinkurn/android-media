package com.tokopedia.seller.opportunity.domain.param;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.seller.opportunity.viewmodel.opportunitylist.FilterPass;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by nisie on 3/2/17.
 */
public class GetOpportunityListParam implements Parcelable {
    private String page;
    private String query;
    private ArrayList<FilterPass> listFilter;

    public GetOpportunityListParam() {
        this.listFilter = new ArrayList<>();
    }

    protected GetOpportunityListParam(Parcel in) {
        page = in.readString();
        query = in.readString();
        listFilter = in.createTypedArrayList(FilterPass.CREATOR);
    }

    public static final Creator<GetOpportunityListParam> CREATOR = new Creator<GetOpportunityListParam>() {
        @Override
        public GetOpportunityListParam createFromParcel(Parcel in) {
            return new GetOpportunityListParam(in);
        }

        @Override
        public GetOpportunityListParam[] newArray(int size) {
            return new GetOpportunityListParam[size];
        }
    };

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

    public ArrayList<FilterPass> getListFilter() {
        return listFilter;
    }

    public void setListFilter(ArrayList<FilterPass> listFilter) {
        this.listFilter = listFilter;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(page);
        dest.writeString(query);
        dest.writeTypedList(listFilter);
    }

    public void setSort(FilterPass sort) {
        this.listFilter.add(sort);
    }
}
