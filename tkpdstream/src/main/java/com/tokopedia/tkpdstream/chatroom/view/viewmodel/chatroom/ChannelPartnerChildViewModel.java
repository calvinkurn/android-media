package com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by milhamj on 26/03/18.
 */

public class ChannelPartnerChildViewModel implements Parcelable {
    private String partnerId;
    private String partnerAvatar;
    private String partnerName;
    private String partnerUrl;

    public ChannelPartnerChildViewModel(String partnerId, String partnerAvatar, String partnerName,
                                        String partnerUrl) {
        this.partnerId =partnerId;
        this.partnerAvatar = partnerAvatar;
        this.partnerName = partnerName;
        this.partnerUrl = partnerUrl;
    }

    public String getPartnerAvatar() {
        return partnerAvatar;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public String getPartnerUrl() {
        return partnerUrl;
    }

    public String getPartnerId() {
        return partnerId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.partnerId);
        dest.writeString(this.partnerAvatar);
        dest.writeString(this.partnerName);
        dest.writeString(this.partnerUrl);
    }

    protected ChannelPartnerChildViewModel(Parcel in) {
        this.partnerId = in.readString();
        this.partnerAvatar = in.readString();
        this.partnerName = in.readString();
        this.partnerUrl = in.readString();
    }

    public static final Parcelable.Creator<ChannelPartnerChildViewModel> CREATOR = new Parcelable
            .Creator<ChannelPartnerChildViewModel>() {
        @Override
        public ChannelPartnerChildViewModel createFromParcel(Parcel source) {
            return new ChannelPartnerChildViewModel(source);
        }

        @Override
        public ChannelPartnerChildViewModel[] newArray(int size) {
            return new ChannelPartnerChildViewModel[size];
        }
    };
}
