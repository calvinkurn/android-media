package com.tokopedia.tkpdstream.chatroom.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by nisie on 2/14/18.
 */

public class GroupChatViewModel implements Parcelable {

    private String channelUrl;

    public GroupChatViewModel(String channelUrl) {
        this.channelUrl = channelUrl;
    }

    public GroupChatViewModel() {
        this.channelUrl = "pub1";
    }

    protected GroupChatViewModel(Parcel in) {
        channelUrl = in.readString();
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

    public String getChannelUrl() {
        return channelUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(channelUrl);
    }
}
