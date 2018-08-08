package com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yfsx on 24/01/18.
 */

public class InboxItemResultViewModel implements Parcelable {
    private List<InboxItemViewModel> inboxItemViewModels;
    private List<FilterViewModel> filterViewModels;
    private boolean isCanLoadMore;

    private FilterListViewModel filterListViewModel;
    private List<Visitable> inboxVisitableList;

    public InboxItemResultViewModel(List<InboxItemViewModel> inboxItemViewModels, List<FilterViewModel> filterViewModels, boolean isCanLoadMore) {
        this.inboxItemViewModels = inboxItemViewModels;
        this.filterViewModels = filterViewModels;
        this.isCanLoadMore = isCanLoadMore;
    }

    public boolean isCanLoadMore() {
        return isCanLoadMore;
    }

    public void setCanLoadMore(boolean canLoadMore) {
        isCanLoadMore = canLoadMore;
    }

    public List<FilterViewModel> getFilterViewModels() {
        return filterViewModels;
    }

    public void setFilterViewModels(List<FilterViewModel> filterViewModels) {
        this.filterViewModels = filterViewModels;
    }

    public List<InboxItemViewModel> getInboxItemViewModels() {
        return inboxItemViewModels;
    }

    public void setInboxItemViewModels(List<InboxItemViewModel> inboxItemViewModels) {
        this.inboxItemViewModels = inboxItemViewModels;
    }

    public FilterListViewModel getFilterListViewModel() {
        return filterListViewModel;
    }

    public void setFilterListViewModel(FilterListViewModel filterListViewModel) {
        this.filterListViewModel = filterListViewModel;
    }

    public List<Visitable> getInboxVisitableList() {
        return inboxVisitableList;
    }

    public void setInboxVisitableList(List<Visitable> inboxVisitableList) {
        this.inboxVisitableList = inboxVisitableList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.inboxItemViewModels);
        dest.writeTypedList(this.filterViewModels);
        dest.writeParcelable(this.filterListViewModel, flags);
        dest.writeList(this.inboxVisitableList);
    }

    protected InboxItemResultViewModel(Parcel in) {
        this.inboxItemViewModels = in.createTypedArrayList(InboxItemViewModel.CREATOR);
        this.filterViewModels = in.createTypedArrayList(FilterViewModel.CREATOR);
        this.filterListViewModel = in.readParcelable(FilterListViewModel.class.getClassLoader());
        this.inboxVisitableList = new ArrayList<Visitable>();
        in.readList(this.inboxVisitableList, Visitable.class.getClassLoader());
    }

    public static final Creator<InboxItemResultViewModel> CREATOR = new Creator<InboxItemResultViewModel>() {
        @Override
        public InboxItemResultViewModel createFromParcel(Parcel source) {
            return new InboxItemResultViewModel(source);
        }

        @Override
        public InboxItemResultViewModel[] newArray(int size) {
            return new InboxItemResultViewModel[size];
        }
    };
}
