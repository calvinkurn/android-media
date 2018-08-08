package com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by yfsx on 06/02/18.
 */

public class SingleItemInboxResultViewModel implements Parcelable {

    private InboxItemViewModel inboxItemViewModel;
    private List<FilterViewModel> filterViewModels;

    private FilterListViewModel filterListViewModel;

    public SingleItemInboxResultViewModel(InboxItemViewModel inboxItemViewModel, List<FilterViewModel> filterViewModels) {
        this.inboxItemViewModel = inboxItemViewModel;
        this.filterViewModels = filterViewModels;
    }

    public InboxItemViewModel getInboxItemViewModel() {
        return inboxItemViewModel;
    }

    public void setInboxItemViewModel(InboxItemViewModel inboxItemViewModel) {
        this.inboxItemViewModel = inboxItemViewModel;
    }

    public List<FilterViewModel> getFilterViewModels() {
        return filterViewModels;
    }

    public void setFilterViewModels(List<FilterViewModel> filterViewModels) {
        this.filterViewModels = filterViewModels;
    }

    public FilterListViewModel getFilterListViewModel() {
        return filterListViewModel;
    }

    public void setFilterListViewModel(FilterListViewModel filterListViewModel) {
        this.filterListViewModel = filterListViewModel;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.inboxItemViewModel, flags);
        dest.writeTypedList(this.filterViewModels);
    }

    protected SingleItemInboxResultViewModel(Parcel in) {
        this.inboxItemViewModel = in.readParcelable(InboxItemViewModel.class.getClassLoader());
        this.filterViewModels = in.createTypedArrayList(FilterViewModel.CREATOR);
    }

    public static final Parcelable.Creator<SingleItemInboxResultViewModel> CREATOR = new Parcelable.Creator<SingleItemInboxResultViewModel>() {
        @Override
        public SingleItemInboxResultViewModel createFromParcel(Parcel source) {
            return new SingleItemInboxResultViewModel(source);
        }

        @Override
        public SingleItemInboxResultViewModel[] newArray(int size) {
            return new SingleItemInboxResultViewModel[size];
        }
    };
}
