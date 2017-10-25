package com.tokopedia.inbox.rescenter.detailv2.view.customadapter.chatadaptercard;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResChatFragmentListener;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatLeftViewModel;

import org.w3c.dom.Text;

/**
 * Created by yoasfs on 23/10/17.
 */

public class ChatLeftViewHolder extends AbstractViewHolder<ChatLeftViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_detail_chat_left;

    DetailResChatFragmentListener.View mainView;
    TextView tvMessage, tvDate;
    RecyclerView rvAttachment;

    public ChatLeftViewHolder(View itemView, DetailResChatFragmentListener.View mainView) {
        super(itemView);
        this.mainView = mainView;
        tvMessage = (TextView) itemView.findViewById(R.id.tv_message);
        tvDate = (TextView) itemView.findViewById(R.id.tv_date);
        rvAttachment = (RecyclerView) itemView.findViewById(R.id.rv_attachment);
    }

    @Override
    public void bind(ChatLeftViewModel element) {
        tvMessage.setText(element.getMessage());
        if (element.getAttachment().size() == 0) {
            rvAttachment.setVisibility(View.GONE);
        }
    }

}
