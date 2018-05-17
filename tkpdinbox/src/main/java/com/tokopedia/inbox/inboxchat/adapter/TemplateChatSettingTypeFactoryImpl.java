package com.tokopedia.inbox.inboxchat.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.inbox.inboxchat.fragment.TemplateChatFragment;
import com.tokopedia.inbox.inboxchat.presenter.TemplateChatContract;
import com.tokopedia.inbox.inboxchat.adapter.viewholder.chattemplate.ItemAddTemplateChatViewHolder;
import com.tokopedia.inbox.inboxchat.adapter.viewholder.chattemplate.ItemTemplateChatViewHolder;
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
