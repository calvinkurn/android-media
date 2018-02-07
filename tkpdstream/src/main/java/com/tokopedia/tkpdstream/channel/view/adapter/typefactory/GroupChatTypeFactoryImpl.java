package com.tokopedia.tkpdstream.channel.view.adapter.typefactory;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpdstream.channel.view.adapter.viewholder.MyChatViewHolder;
import com.tokopedia.tkpdstream.channel.view.adapter.viewholder.OppositeChatViewHolder;
import com.tokopedia.tkpdstream.channel.view.fragment.GroupChatFragment;
import com.tokopedia.tkpdstream.channel.view.viewmodel.MyChatViewModel;
import com.tokopedia.tkpdstream.channel.view.viewmodel.OppositeChatViewModel;

/**
 * @author by nisie on 2/7/18.
 */

public class GroupChatTypeFactoryImpl extends BaseAdapterTypeFactory implements GroupChatTypeFactory {

    public GroupChatTypeFactoryImpl(GroupChatFragment context) {

    }

    @Override
    public int type(OppositeChatViewModel oppositeChatViewModel) {
        return OppositeChatViewHolder.LAYOUT;
    }

    @Override
    public int type(MyChatViewModel myChatViewModel) {
        return MyChatViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        AbstractViewHolder viewHolder;

        if (type == MyChatViewHolder.LAYOUT) {
            viewHolder = new MyChatViewHolder(parent);
        } else if (type == OppositeChatViewHolder.LAYOUT) {
            viewHolder = new OppositeChatViewHolder(parent);
        } else {
            viewHolder = super.createViewHolder(parent, type);
        }
        return viewHolder;
    }
}
