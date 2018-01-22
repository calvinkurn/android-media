package com.tokopedia.inbox.inboxchat.adapter;

import android.view.View;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.inbox.inboxchat.viewmodel.TemplateChatModel;

/**
 * Created by stevenfredian on 9/27/17.
 */

public interface TemplateChatSettingTypeFactory extends TemplateChatTypeFactory{

    AbstractViewHolder createViewHolder(View view, int viewType);

    int type(TemplateChatModel templateChatModel);
}
