package com.tokopedia.tkpdstream.chatroom.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by nisie on 2/14/18.
 */

public class GroupChatViewModel implements Parcelable {

    private String channelUuid;
    private ChannelInfoViewModel channelInfoViewModel;

    public GroupChatViewModel(String channelUuid) {
        this.channelUuid = channelUuid;
        this.channelInfoViewModel = null;
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
        if (channelInfoViewModel != null) {
            this.channelInfoViewModel.setTotalParticipant(totalParticipant);
        }
    }

    public int getTotalParticipant() {
        return channelInfoViewModel != null ? channelInfoViewModel.getTotalParticipantsOnline() : 0;
    }

    public String getChannelName() {
        return channelInfoViewModel != null ? channelInfoViewModel.getTitle() : "";
    }


    public String getChannelUrl() {
        return channelInfoViewModel != null ? channelInfoViewModel.getChannelUrl() : "";
    }

    public void setChannelInfo(ChannelInfoViewModel channelInfoViewModel) {
        this.channelInfoViewModel = channelInfoViewModel;
    }

    public String getPollId() {
        if (channelInfoViewModel != null
                && channelInfoViewModel.getVoteInfoViewModel() != null) {
            return this.channelInfoViewModel.getVoteInfoViewModel().getPollId();
        } else {
            return "";
        }
    }

    public ChannelInfoViewModel getChannelInfoViewModel() {
        return channelInfoViewModel;
    }
}
