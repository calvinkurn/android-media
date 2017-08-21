package com.tokopedia.tkpd.tkpdreputation.inbox.view.fragment;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.FilterViewModel;

import java.util.ArrayList;

/**
 * @author by nisie on 8/21/17.
 */

public class InboxReputationFilterViewModel implements Parcelable {
    private ArrayList<FilterViewModel> listFilter;

    public InboxReputationFilterViewModel(ArrayList<FilterViewModel> listFilter) {
        this.listFilter = listFilter;
    }

    protected InboxReputationFilterViewModel(Parcel in) {
        listFilter = in.createTypedArrayList(FilterViewModel.CREATOR);
    }

    public static final Creator<InboxReputationFilterViewModel> CREATOR =
            new Creator<InboxReputationFilterViewModel>() {
                @Override
                public InboxReputationFilterViewModel createFromParcel(Parcel in) {
                    return new InboxReputationFilterViewModel(in);
                }

                @Override
                public InboxReputationFilterViewModel[] newArray(int size) {
                    return new InboxReputationFilterViewModel[size];
                }
            };

    public ArrayList<FilterViewModel> getListFilter() {
        return listFilter;
    }

    public void setListFilter(ArrayList<FilterViewModel> listFilter) {
        this.listFilter = listFilter;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(listFilter);
    }
}
