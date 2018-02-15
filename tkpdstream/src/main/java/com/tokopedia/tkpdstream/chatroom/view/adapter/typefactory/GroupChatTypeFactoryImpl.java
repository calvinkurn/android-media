package com.tokopedia.tkpdstream.chatroom.view.adapter.typefactory;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpdstream.chatroom.view.fragment.GroupChatFragment;
import com.tokopedia.tkpdstream.chatroom.view.adapter.viewholder.ChatViewHolder;
import com.tokopedia.tkpdstream.chatroom.view.adapter.viewholder.AdminAnnouncementViewHolder;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.ChatViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.AdminAnnouncementViewModel;

/**
 * @author by nisie on 2/7/18.
 */

public class GroupChatTypeFactoryImpl extends BaseAdapterTypeFactory implements GroupChatTypeFactory {

    public GroupChatTypeFactoryImpl(GroupChatFragment context) {

    }

    @Override
    public int type(AdminAnnouncementViewModel oppositeChatViewModel) {
        return AdminAnnouncementViewHolder.LAYOUT;
    }

    @Override
    public int type(ChatViewModel myChatViewModel) {
        return ChatViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        AbstractViewHolder viewHolder;

        if (type == ChatViewHolder.LAYOUT) {
            viewHolder = new ChatViewHolder(parent);
        } else if (type == AdminAnnouncementViewHolder.LAYOUT) {
            viewHolder = new AdminAnnouncementViewHolder(parent);
        } else {
            viewHolder = super.createViewHolder(parent, type);
        }
        return viewHolder;
    }
}
