package com.tokopedia.inbox.inboxchat.adapter;

import android.view.View;

import com.tokopedia.core.base.adapter.BaseAdapterTypeFactory;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.inbox.inboxchat.fragment.ChatRoomFragment;
import com.tokopedia.inbox.inboxchat.fragment.TemplateChatFragment;
import com.tokopedia.inbox.inboxchat.presenter.ChatRoomContract;
import com.tokopedia.inbox.inboxchat.presenter.TemplateChatContract;
import com.tokopedia.inbox.inboxchat.viewholder.ItemAddTemplateChatViewHolder;
import com.tokopedia.inbox.inboxchat.viewholder.ItemTemplateChatViewHolder;
import com.tokopedia.inbox.inboxchat.viewholder.TemplateChatViewHolder;
import com.tokopedia.inbox.inboxchat.viewmodel.TemplateChatModel;

/**
 * Created by stevenfredian on 9/27/17.
 */

public class TemplateChatSettingTypeFactoryImpl extends BaseAdapterTypeFactory implements TemplateChatSettingTypeFactory {

    TemplateChatContract.View viewListener;

    public TemplateChatSettingTypeFactoryImpl(TemplateChatFragment context) {
        this.viewListener = context;
    }

    @Override
    public AbstractViewHolder createViewHolder(View view, int type) {

        AbstractViewHolder viewHolder;

        if (type == ItemTemplateChatViewHolder.LAYOUT) {
            viewHolder = new ItemTemplateChatViewHolder(view, viewListener);
        } else if (type == ItemAddTemplateChatViewHolder.LAYOUT) {
            viewHolder = new ItemAddTemplateChatViewHolder(view, viewListener);
        } else {
            return super.createViewHolder(view, type);
        }
        return viewHolder;
    }

    @Override
    public int type(TemplateChatModel templateChatModel) {
        if (templateChatModel.isIcon()) {
            return ItemAddTemplateChatViewHolder.LAYOUT;
        }
        return ItemTemplateChatViewHolder.LAYOUT;
    }

}
