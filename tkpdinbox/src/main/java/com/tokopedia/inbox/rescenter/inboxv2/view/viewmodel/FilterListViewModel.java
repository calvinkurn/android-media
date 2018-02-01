package com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.inbox.rescenter.inboxv2.view.adapter.typefactory.ResoInboxTypeFactory;

import java.util.List;

/**
 * Created by yfsx on 01/02/18.
 */

public class FilterListViewModel implements Visitable<ResoInboxTypeFactory>, Parcelable {
    private List<FilterViewModel> filterList;

    @Override
    public int type(ResoInboxTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public FilterListViewModel(List<FilterViewModel> filterList) {
        this.filterList = filterList;
    }

    public List<FilterViewModel> getFilterList() {
        return filterList;
    }

    public void setFilterList(List<FilterViewModel> filterList) {
        this.filterList = filterList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.filterList);
    }

    protected FilterListViewModel(Parcel in) {
        this.filterList = in.createTypedArrayList(FilterViewModel.CREATOR);
    }

    public static final Parcelable.Creator<FilterListViewModel> CREATOR = new Parcelable.Creator<FilterListViewModel>() {
        @Override
        public FilterListViewModel createFromParcel(Parcel source) {
            return new FilterListViewModel(source);
        }

        @Override
        public FilterListViewModel[] newArray(int size) {
            return new FilterListViewModel[size];
        }
    };
}
