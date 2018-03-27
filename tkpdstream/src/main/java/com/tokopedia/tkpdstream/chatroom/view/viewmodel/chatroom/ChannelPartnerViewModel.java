package com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by milhamj on 26/03/18.
 */

public class ChannelPartnerViewModel implements Parcelable {
    private String partnerTitle;
    private List<ChannelPartnerChildViewModel> child;

    public ChannelPartnerViewModel(String partnerTitle, List<ChannelPartnerChildViewModel> child) {
        this.partnerTitle = partnerTitle;
        this.child = child;
    }

    public String getPartnerTitle() {
        return partnerTitle;
    }

    public List<ChannelPartnerChildViewModel> getChild() {
        return child;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.partnerTitle);
        dest.writeList(this.child);
    }

    protected ChannelPartnerViewModel(Parcel in) {
        this.partnerTitle = in.readString();
        this.child = new ArrayList<ChannelPartnerChildViewModel>();
        in.readList(this.child, ChannelPartnerChildViewModel.class.getClassLoader());
    }

    public static final Parcelable.Creator<ChannelPartnerViewModel> CREATOR = new Parcelable
            .Creator<ChannelPartnerViewModel>() {
        @Override
        public ChannelPartnerViewModel createFromParcel(Parcel source) {
            return new ChannelPartnerViewModel(source);
        }

        @Override
        public ChannelPartnerViewModel[] newArray(int size) {
            return new ChannelPartnerViewModel[size];
        }
    };
}
