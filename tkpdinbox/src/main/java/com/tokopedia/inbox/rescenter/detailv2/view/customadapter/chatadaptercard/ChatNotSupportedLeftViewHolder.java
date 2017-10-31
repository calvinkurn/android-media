package com.tokopedia.inbox.rescenter.detailv2.view.customadapter.chatadaptercard;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResChatFragmentListener;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatNotSupportedLeftViewModel;

/**
 * Created by yoasfs on 23/10/17.
 */

public class ChatNotSupportedLeftViewHolder extends AbstractViewHolder<ChatNotSupportedLeftViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_detail_notsupported_left;

    DetailResChatFragmentListener.View mainView;

    TextView tvMessage;

    public ChatNotSupportedLeftViewHolder(View itemView, DetailResChatFragmentListener.View mainView) {
        super(itemView);
        this.mainView = mainView;
        tvMessage = (TextView) itemView.findViewById(R.id.tv_message);
    }

    @Override
    public void bind(ChatNotSupportedLeftViewModel element) {
        tvMessage.setText("Not supported type : " + element.getConversation().getAction().getType());
    }
}
