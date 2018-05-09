package com.tokopedia.inbox.inboxchat.adapter.viewholder;

import android.view.View;

import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.inboxchat.domain.model.websocket.BaseChatViewModel;
import com.tokopedia.inbox.inboxchat.domain.model.websocket.FallbackAttachmentViewModel;
import com.tokopedia.inbox.inboxchat.presenter.ChatRoomContract;

/**
 * @author by nisie on 5/9/18.
 */
public class FallbackAttachmentViewHolder extends BaseChatViewHolder<FallbackAttachmentViewModel> {

    public static final int LAYOUT = R.layout.layout_fallback_attachment;

    public FallbackAttachmentViewHolder(View itemView, ChatRoomContract.View viewListener) {
        super(itemView, viewListener);
    }

    @Override
    public void bind(FallbackAttachmentViewModel viewModel) {
        super.bind(viewModel);
    }

    @Override
    protected void setMessage(BaseChatViewModel element) {
        FallbackAttachmentViewModel viewModel = (FallbackAttachmentViewModel) element;
        message.setText(MethodChecker.fromHtml(viewModel.getMessage()));
    }
}
