package com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by yfsx on 24/01/18.
 */

public class InboxItemResultViewModel implements Parcelable {
    private List<InboxItemViewModel> inboxItemViewModels;

    public InboxItemResultViewModel(List<InboxItemViewModel> inboxItemViewModels) {
        this.inboxItemViewModels = inboxItemViewModels;
    }

    public List<InboxItemViewModel> getInboxItemViewModels() {
        return inboxItemViewModels;
    }

    public void setInboxItemViewModels(List<InboxItemViewModel> inboxItemViewModels) {
        this.inboxItemViewModels = inboxItemViewModels;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.inboxItemViewModels);
    }

    protected InboxItemResultViewModel(Parcel in) {
        this.inboxItemViewModels = in.createTypedArrayList(InboxItemViewModel.CREATOR);
    }

    public static final Parcelable.Creator<InboxItemResultViewModel> CREATOR = new Parcelable.Creator<InboxItemResultViewModel>() {
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
