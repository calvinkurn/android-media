package com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.tkpdstream.chatroom.view.adapter.chatroom.typefactory.GroupChatTypeFactory;
import com.tokopedia.tkpdstream.vote.view.model.VoteInfoViewModel;

/**
 * @author by nisie on 2/27/18.
 */

public class VoteAnnouncementViewModel extends BaseChatViewModel implements Visitable<GroupChatTypeFactory>, Parcelable {

    public static final String POLLING_START = "polling_start";
    public static final String POLLING_FINISHED = "polling_finish";
    public static final String POLLING_CANCEL = "polling_cancel";
    public static final String POLLING_UPDATE = "polling_update";

    private String voteType;
    private VoteInfoViewModel voteInfoViewModel;


    public VoteAnnouncementViewModel(String message, String voteType, long createdAt,
                                     long updatedAt, String messageId, String senderId,
                                     String senderName, String senderIconUrl,
                                     boolean isInfluencer, boolean isAdministrator,
                                     VoteInfoViewModel voteInfoViewModel) {
        super(message, createdAt, updatedAt, messageId, senderId, senderName, senderIconUrl,
                isInfluencer, isAdministrator);
        this.voteType = voteType;
        this.voteInfoViewModel = voteInfoViewModel;
    }

    public String getVoteType() {
        return voteType;
    }

    @Override
    public int type(GroupChatTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public VoteInfoViewModel getVoteInfoViewModel() {
        return voteInfoViewModel;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.voteType);
        dest.writeParcelable(this.voteInfoViewModel, flags);
    }

    protected VoteAnnouncementViewModel(Parcel in) {
        super(in);
        this.voteType = in.readString();
        this.voteInfoViewModel = in.readParcelable(VoteInfoViewModel.class.getClassLoader());
    }

    public static final Creator<VoteAnnouncementViewModel> CREATOR = new Creator<VoteAnnouncementViewModel>() {
        @Override
        public VoteAnnouncementViewModel createFromParcel(Parcel source) {
            return new VoteAnnouncementViewModel(source);
        }

        @Override
        public VoteAnnouncementViewModel[] newArray(int size) {
            return new VoteAnnouncementViewModel[size];
        }
    };
}
