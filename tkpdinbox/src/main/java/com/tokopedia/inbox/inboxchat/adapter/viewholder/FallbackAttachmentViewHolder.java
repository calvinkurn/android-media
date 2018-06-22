package com.tokopedia.inbox.inboxchat.adapter.viewholder;

import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.inboxchat.domain.model.websocket.FallbackAttachmentViewModel;
import com.tokopedia.inbox.inboxchat.presenter.ChatRoomContract;
import com.tokopedia.inbox.inboxchat.viewholder.movement.ChatLinkHandlerMovementMethod;

/**
 * @author by nisie on 5/9/18.
 */
public class FallbackAttachmentViewHolder extends BaseChatViewHolder<FallbackAttachmentViewModel> {

    public static final int LAYOUT = R.layout.layout_fallback_attachment;

    private TextView message;

    public FallbackAttachmentViewHolder(View itemView, ChatRoomContract.View viewListener) {
        super(itemView, viewListener);
        message = (TextView) itemView.findViewById(R.id.message);
    }

    @Override
    public void bind(FallbackAttachmentViewModel viewModel) {
        super.bind(viewModel);
        setMessage(viewModel);
        setClickableUrl();
    }

    private void setMessage(FallbackAttachmentViewModel element) {
        if (!element.getMessage().isEmpty()) {
            message.setText(MethodChecker.fromHtml(element.getMessage()));
        }
    }

    private void setClickableUrl() {
        message.setMovementMethod(new ChatLinkHandlerMovementMethod(viewListener));
    }
}
