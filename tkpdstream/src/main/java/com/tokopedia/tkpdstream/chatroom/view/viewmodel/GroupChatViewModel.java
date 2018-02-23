package com.tokopedia.tkpdstream.chatroom.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by nisie on 2/14/18.
 */

public class GroupChatViewModel implements Parcelable {

    private String channelUuid;
    private int totalParticipant;
    private String channelName;
    private String channelUrl;

    public GroupChatViewModel(String channelUuid) {
        this.channelUuid = channelUuid;
        this.channelName = "";
        this.totalParticipant = 0;
    }

    protected GroupChatViewModel(Parcel in) {
        channelUuid = in.readString();
    }

    public static final Creator<GroupChatViewModel> CREATOR = new Creator<GroupChatViewModel>() {
        @Override
        public GroupChatViewModel createFromParcel(Parcel in) {
            return new GroupChatViewModel(in);
        }

        @Override
        public GroupChatViewModel[] newArray(int size) {
            return new GroupChatViewModel[size];
        }
    };

    public String getChannelUuid() {
        return channelUuid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(channelUuid);
    }

    public void setTotalParticipant(int totalParticipant) {
        this.totalParticipant = totalParticipant;
    }

    public int getTotalParticipant() {
        return totalParticipant;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public void setChannelUrl(String channelUrl) {
        this.channelUrl = channelUrl;
    }

    public String getChannelUrl() {
        return channelUrl;
    }
}
