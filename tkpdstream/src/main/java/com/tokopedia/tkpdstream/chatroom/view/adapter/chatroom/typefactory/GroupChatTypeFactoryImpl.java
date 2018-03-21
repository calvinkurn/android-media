package com.tokopedia.tkpdstream.chatroom.view.adapter.chatroom.typefactory;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpdstream.chatroom.view.adapter.chatroom.viewholder.AdminAnnouncementViewHolder;
import com.tokopedia.tkpdstream.chatroom.view.adapter.chatroom.viewholder.ChatViewHolder;
import com.tokopedia.tkpdstream.chatroom.view.adapter.chatroom.viewholder.ImageViewHolder;
import com.tokopedia.tkpdstream.chatroom.view.adapter.chatroom.viewholder.PendingChatViewHolder;
import com.tokopedia.tkpdstream.chatroom.view.adapter.chatroom.viewholder.UserActionViewHolder;
import com.tokopedia.tkpdstream.chatroom.view.adapter.chatroom.viewholder.VoteAnnouncementViewHolder;
import com.tokopedia.tkpdstream.chatroom.view.fragment.GroupChatFragment;
import com.tokopedia.tkpdstream.chatroom.view.listener.GroupChatContract;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.AdminAnnouncementViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.ChatViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.ImageViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.PendingChatViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.UserActionViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.VoteAnnouncementViewModel;

/**
 * @author by nisie on 2/7/18.
 */

public class GroupChatTypeFactoryImpl extends BaseAdapterTypeFactory implements GroupChatTypeFactory {

    GroupChatContract.View.ImageViewHolderListener imageListener;
    GroupChatContract.View.VoteAnnouncementViewHolderListener voteAnnouncementViewHolderListener;

    public GroupChatTypeFactoryImpl(GroupChatFragment fragment) {
        imageListener = fragment;
        voteAnnouncementViewHolderListener = fragment;
    }

    @Override
    public int type(AdminAnnouncementViewModel adminAnnouncementViewModel) {
        return AdminAnnouncementViewHolder.LAYOUT;
    }

    @Override
    public int type(ChatViewModel myChatViewModel) {
        return ChatViewHolder.LAYOUT;
    }

    @Override
    public int type(PendingChatViewModel pendingChatViewModel) {
        return PendingChatViewHolder.LAYOUT;
    }

    @Override
    public int type(UserActionViewModel userActionViewModel) {
        return UserActionViewHolder.LAYOUT;
    }

    @Override
    public int type(ImageViewModel imageViewModel) {
        return ImageViewHolder.LAYOUT;
    }

    @Override
    public int type(VoteAnnouncementViewModel voteAnnouncementViewModel) {
        return VoteAnnouncementViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        AbstractViewHolder viewHolder;

        if (type == ChatViewHolder.LAYOUT) {
            viewHolder = new ChatViewHolder(parent);
        } else if (type == AdminAnnouncementViewHolder.LAYOUT) {
            viewHolder = new AdminAnnouncementViewHolder(parent);
        } else if (type == PendingChatViewHolder.LAYOUT) {
            viewHolder = new PendingChatViewHolder(parent);
        } else if (type == UserActionViewHolder.LAYOUT) {
            viewHolder = new UserActionViewHolder(parent);
        } else if (type == ImageViewHolder.LAYOUT) {
            viewHolder = new ImageViewHolder(parent, imageListener);
        } else if (type == VoteAnnouncementViewHolder.LAYOUT) {
            viewHolder = new VoteAnnouncementViewHolder(parent, voteAnnouncementViewHolderListener);
        } else {
            viewHolder = super.createViewHolder(parent, type);
        }
        return viewHolder;
    }
}
