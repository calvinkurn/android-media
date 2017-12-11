package com.tokopedia.inbox.inboxchat.adapter;

import android.view.View;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.inbox.inboxchat.viewmodel.AttachImageModel;
import com.tokopedia.inbox.inboxchat.viewmodel.MyChatViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.OppositeChatViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.TemplateChatModel;
import com.tokopedia.inbox.inboxchat.viewmodel.TypingChatModel;
import com.tokopedia.inbox.inboxchat.viewmodel.chatroom.TimeMachineChatModel;

/**
 * Created by stevenfredian on 9/27/17.
 */

public interface TemplateChatTypeFactory {

    AbstractViewHolder createViewHolder(View view, int viewType);

    int type(TemplateChatModel templateChatModel);
}
