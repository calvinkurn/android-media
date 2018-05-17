package com.tokopedia.inbox.inboxchat.adapter.viewholder.chatbot;

import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.inboxchat.adapter.viewholder.common.BaseChatViewHolder;
import com.tokopedia.inbox.inboxchat.presenter.ChatRoomContract;
import com.tokopedia.inbox.inboxchat.util.ChatLinkHandlerMovementMethod;
import com.tokopedia.inbox.inboxchat.viewmodel.chatroom.quickreply.QuickReplyListViewModel;

/**
 * @author by nisie on 5/8/18.
 */
public class QuickReplyViewHolder extends BaseChatViewHolder<QuickReplyListViewModel> {

    public static final int LAYOUT = R.layout.quick_reply_chat_layout;

    private TextView message;

    public QuickReplyViewHolder(View itemView, ChatRoomContract.View viewListener) {
        super(itemView, viewListener);
        message = (TextView) itemView.findViewById(R.id.message);
    }

    @Override
    public void bind(QuickReplyListViewModel element) {
        super.bind(element);
        setMessage(element);
        setClickableUrl();
    }

    private void setMessage(QuickReplyListViewModel element) {
        if (!element.getMessage().isEmpty()) {
            message.setText(MethodChecker.fromHtml(element.getMessage()));
        }
    }

    private void setClickableUrl() {
        message.setMovementMethod(new ChatLinkHandlerMovementMethod(viewListener));
    }
}
