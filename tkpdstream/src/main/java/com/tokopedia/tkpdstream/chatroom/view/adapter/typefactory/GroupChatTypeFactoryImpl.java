package com.tokopedia.tkpdstream.chatroom.view.adapter.typefactory;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpdstream.chatroom.view.adapter.viewholder.AdminAnnouncementViewHolder;
import com.tokopedia.tkpdstream.chatroom.view.adapter.viewholder.ChatViewHolder;
import com.tokopedia.tkpdstream.chatroom.view.adapter.viewholder.ImageViewHolder;
import com.tokopedia.tkpdstream.chatroom.view.adapter.viewholder.PendingChatViewHolder;
import com.tokopedia.tkpdstream.chatroom.view.adapter.viewholder.UserActionViewHolder;
import com.tokopedia.tkpdstream.chatroom.view.fragment.GroupChatFragment;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.AdminAnnouncementViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.ChatViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.ImageViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.PendingChatViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.UserActionViewModel;

/**
 * @author by nisie on 2/7/18.
 */

public class GroupChatTypeFactoryImpl extends BaseAdapterTypeFactory implements GroupChatTypeFactory {

    public GroupChatTypeFactoryImpl(GroupChatFragment context) {

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
            viewHolder = new ImageViewHolder(parent);
        } else {
            viewHolder = super.createViewHolder(parent, type);
        }
        return viewHolder;
    }
}
