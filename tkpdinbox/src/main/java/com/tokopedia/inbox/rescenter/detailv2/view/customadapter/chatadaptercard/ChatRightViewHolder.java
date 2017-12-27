package com.tokopedia.inbox.rescenter.detailv2.view.customadapter.chatadaptercard;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.util.DateFormatUtils;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.detailv2.view.customadapter.ChatProductGeneralAdapter;
import com.tokopedia.inbox.rescenter.detailv2.view.customadapter.ChatProveAdapter;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResChatFragmentListener;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatRightViewModel;

/**
 * Created by yoasfs on 23/10/17.
 */

public class ChatRightViewHolder extends AbstractViewHolder<ChatRightViewModel> {

    public static final int COUNT_MAX_PRODUCT = 5;
    @LayoutRes
    public static final int LAYOUT = R.layout.item_detail_chat_right;

    DetailResChatFragmentListener.View mainView;
    View layoutDate;
    TextView tvMessage, tvDate;
    RecyclerView rvAttachment;
    ChatProductGeneralAdapter adapter;

    public ChatRightViewHolder(View itemView, DetailResChatFragmentListener.View mainView) {
        super(itemView);
        this.mainView = mainView;
        tvMessage = (TextView) itemView.findViewById(R.id.tv_message);
        rvAttachment = (RecyclerView) itemView.findViewById(R.id.rv_attachment);
        layoutDate = itemView.findViewById(R.id.layout_date);
        tvDate = (TextView) layoutDate.findViewById(R.id.tv_date);
    }

    @Override
    public void bind(ChatRightViewModel element) {
        if (element.getConversation().getMessage() != null) {
            tvMessage.setText(MethodChecker.fromHtml(element.getConversation().getMessage()));
        }
        if (element.getConversation().getAttachment() == null || element.getConversation().getAttachment().size() == 0) {
            rvAttachment.setVisibility(View.GONE);
        } else {
            rvAttachment.setVisibility(View.VISIBLE);
            adapter = new ChatProductGeneralAdapter(mainView, itemView.getContext(), element.getConversation().getAttachment(), COUNT_MAX_PRODUCT);
            rvAttachment.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
            rvAttachment.setAdapter(adapter);
        }
        rvAttachment.setHasFixedSize(true);
        String date = element.getConversation().getCreateTime().getTimestamp();
        tvDate.setText(date);
    }

}
