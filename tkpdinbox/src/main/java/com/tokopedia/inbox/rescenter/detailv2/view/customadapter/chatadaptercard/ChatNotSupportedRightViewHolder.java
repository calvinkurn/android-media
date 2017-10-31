package com.tokopedia.inbox.rescenter.detailv2.view.customadapter.chatadaptercard;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResChatFragmentListener;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatNotSupportedRightViewModel;

/**
 * Created by yoasfs on 23/10/17.
 */

public class ChatNotSupportedRightViewHolder extends AbstractViewHolder<ChatNotSupportedRightViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_detail_notsupported_right;

    DetailResChatFragmentListener.View mainView;

    TextView tvMessage;

    public ChatNotSupportedRightViewHolder(View itemView, DetailResChatFragmentListener.View mainView) {
        super(itemView);
        this.mainView = mainView;
        tvMessage = (TextView) itemView.findViewById(R.id.tv_message);
    }

    @Override
    public void bind(ChatNotSupportedRightViewModel element) {
        tvMessage.setText("Not supported type : " + element.getConversation().getAction().getType());
    }
}
